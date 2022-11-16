package de.viadee.bpm.camunda.processinterface;

import io.camunda.zeebe.client.ZeebeClient;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import model.JsonDataType;
import model.DamageReport;
import org.camunda.bpm.engine.RuntimeService;
import org.camunda.bpm.engine.variable.Variables;
import org.camunda.spin.plugin.variable.SpinValues;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

import static de.viadee.bpm.camunda.processcontext.VariableConstants.EXT_IN_DOCUMENTS;
import static de.viadee.bpm.camunda.processcontext.VariableConstants.EXT_IN_DAMAGE_REPORT;
import static java.lang.String.format;

@Slf4j
@RequiredArgsConstructor
@RestController
public class ProcessStartRestController {
  private static final String LOCALHOST_COCKPIT_URL = "http://localhost:8080/camunda/app/cockpit/default/#/process-instance";
  private static final String OPERATE_URL = "https://%s.operate.camunda.io/%s/processes";
  private static final String CLAIM_PROCESS_EXTERNAL = "claim-processing-external-tasks";
  private static final String CLAIM_PROCESS_DELEGATES = "claim-processing-java-delegates";
  private static final String CLAIM_PROCESS_ZEEBE = "claim-processing-zeebe-job-worker";

  private final RuntimeService runtimeService;
  private final ZeebeClient zeebeClient;

  @PostMapping("/damage-report")
  public ResponseEntity<String> damageReport(@RequestBody final DamageReport damageReport) {
    log.info("Damage report received, vsnr={}", damageReport.getVsnr());

    var withVariables = Variables.createVariables()
                                 .putValue(EXT_IN_DAMAGE_REPORT, SpinValues.jsonValue(damageReport.toJsonString()))
                                 .putValue(EXT_IN_DOCUMENTS, JsonDataType.toJsonList(damageReport.getDocuments())); // multi-instance later

    // start process with java-delegates
    var processInstanceJavaDelegates = runtimeService.startProcessInstanceByMessage(CLAIM_PROCESS_DELEGATES, withVariables);
    var delegateInstanceUrl = format(LOCALHOST_COCKPIT_URL + "/%s", processInstanceJavaDelegates.getId());
    log.info("Java-Delegate-Process-Instance, {}", delegateInstanceUrl);

    // start process with external-tasks
    var processInstanceExternalTasks = runtimeService.startProcessInstanceByMessage(CLAIM_PROCESS_EXTERNAL, withVariables);
    var handlerInstanceUrl = format(LOCALHOST_COCKPIT_URL + "/%s", processInstanceExternalTasks.getId());
    log.info("External-Task-Process-Instance, {}", handlerInstanceUrl);

    // start camunda 8 process
    var processInstanceZeebe = zeebeClient.newCreateInstanceCommand()
                                          .bpmnProcessId(CLAIM_PROCESS_ZEEBE)
                                          .latestVersion()
                                          .variables(
                                            Map.of(
                                              EXT_IN_DAMAGE_REPORT, damageReport,
                                              EXT_IN_DOCUMENTS, damageReport.getDocuments()))
                                          .send().join();

    var clusterRegion = zeebeClient.getConfiguration().getGatewayAddress().split("\\.")[1];
    var clusterId = zeebeClient.getConfiguration().getGatewayAddress().split("\\.")[0];
    var zeebeInstanceUrl = format(OPERATE_URL + "/%s", clusterRegion, clusterId, processInstanceZeebe.getProcessInstanceKey());
    log.info("Camunda-8-SaaS-Process-Instance, {}", zeebeInstanceUrl);

    return new ResponseEntity<>(format("" +
        " Java-Delegate-Process-Instance: %s \n" +
        " External-Task-Process-Instance: %s \n" +
        "Camunda-8-SaaS-Process-Instance: %s \n",
      delegateInstanceUrl, handlerInstanceUrl, zeebeInstanceUrl), HttpStatus.CREATED);
  }
}
