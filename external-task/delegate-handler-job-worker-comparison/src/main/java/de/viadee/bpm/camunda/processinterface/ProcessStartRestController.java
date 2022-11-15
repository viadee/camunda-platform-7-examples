package de.viadee.bpm.camunda.processinterface;

import io.camunda.zeebe.client.ZeebeClient;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import model.JsonDataType;
import model.Schadenmeldung;
import org.camunda.bpm.engine.RuntimeService;
import org.camunda.bpm.engine.variable.Variables;
import org.camunda.spin.plugin.variable.SpinValues;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

import static de.viadee.bpm.camunda.processcontext.VariableConstants.EXT_IN_DOKUMENTE;
import static de.viadee.bpm.camunda.processcontext.VariableConstants.EXT_IN_SCHADENMELDUNG;

@Slf4j
@RequiredArgsConstructor
@RestController
public class ProcessStartRestController {
  public static final String PROCESS_ID_SCHADENMELDUNG_EXTERNAL_TASKS = "schadenmeldung-external-tasks";
  public static final String PROCESS_ID_SCHADENMELDUNG_JAVA_DELEGATES = "schadenmeldung-java-delegates";
  public static final String PROCESS_ID_SCHADENMELDUNG_ZEEBE_WORKERS = "schadenmeldung-zeebe-job-worker-process";

  private final RuntimeService runtimeService;
  private final ZeebeClient zeebeClient;

  @PostMapping("/schadenmeldung")
  public ResponseEntity<String> schadenmeldung(@RequestBody final Schadenmeldung schadenmeldung) {
    log.info("Schadenmeldung eingetroffen, Vsnr={}", schadenmeldung.getVsnr());
    var withVariables = Variables.createVariables()
                                 .putValue(EXT_IN_SCHADENMELDUNG, SpinValues.jsonValue(schadenmeldung.toJsonString()))
                                 .putValue(EXT_IN_DOKUMENTE, JsonDataType.toJsonList(schadenmeldung.getDokumente()));

    var processInstanceJavaDelegates = runtimeService.startProcessInstanceByMessage(PROCESS_ID_SCHADENMELDUNG_JAVA_DELEGATES, withVariables);
    var delegateInstanceUrl = String.format("http://localhost:8080/camunda/app/cockpit/default/#/process-instance/%s", processInstanceJavaDelegates.getId());
    log.info("Java-Delegate-Process-Instance, {}", delegateInstanceUrl);

    var processInstanceExternalTasks = runtimeService.startProcessInstanceByMessage(PROCESS_ID_SCHADENMELDUNG_EXTERNAL_TASKS, withVariables);
    var handlerInstanceUrl = String.format("http://localhost:8080/camunda/app/cockpit/default/#/process-instance/%s", processInstanceExternalTasks.getId());
    log.info("External-Task-Process-Instance, {}", handlerInstanceUrl);

    var processInstanceZeebe = zeebeClient.newCreateInstanceCommand()
                                          .bpmnProcessId(PROCESS_ID_SCHADENMELDUNG_ZEEBE_WORKERS)
                                          .latestVersion()
                                          .variables(
                                            Map.of(
                                              EXT_IN_SCHADENMELDUNG, schadenmeldung,
                                              EXT_IN_DOKUMENTE, schadenmeldung.getDokumente()))
                                          .send().join();

    var clusterRegion = zeebeClient.getConfiguration().getGatewayAddress().split("\\.")[1];
    var clusterId = zeebeClient.getConfiguration().getGatewayAddress().split("\\.")[0];
    var zeebeInstanceUrl = String.format("https://%s.operate.camunda.io/%s/processes/%s", clusterRegion, clusterId, processInstanceZeebe.getProcessInstanceKey());
    log.info("Camunda-8-SaaS-Process-Instance, {}", zeebeInstanceUrl);

    return new ResponseEntity<>(String.format("" +
        " Java-Delegate-Process-Instance: %s \n" +
        " External-Task-Process-Instance: %s \n" +
        "Camunda-8-SaaS-Process-Instance: %s \n",
      delegateInstanceUrl, handlerInstanceUrl, zeebeInstanceUrl), HttpStatus.CREATED);
  }
}
