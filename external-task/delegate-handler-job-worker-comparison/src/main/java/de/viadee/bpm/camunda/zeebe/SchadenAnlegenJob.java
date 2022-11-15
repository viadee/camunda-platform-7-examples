package de.viadee.bpm.camunda.zeebe;

import de.viadee.bpm.camunda.processcontext.ProcessContext;
import de.viadee.bpm.camunda.service.SchadenService;
import io.camunda.zeebe.client.api.response.ActivatedJob;
import io.camunda.zeebe.spring.client.annotation.JobWorker;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Map;

import static de.viadee.bpm.camunda.processcontext.VariableConstants.EXT_OUT_SCHADEN_ID;

@Slf4j
@Component
@RequiredArgsConstructor
public class SchadenAnlegenJob {

  private final SchadenService schadenService;

  @JobWorker(type = "schaden-anlegen")
  public Map<String, Object> schadenAnlegen(final ActivatedJob job) {

    var context = new ProcessContext(job);
    var schadenmeldung = context.getSchadenmeldung();
    var vertrag = context.getVertrag();

    var schadenId = schadenService.schadenAnlegen(schadenmeldung, vertrag);

    return Map.of(EXT_OUT_SCHADEN_ID, schadenId);
  }

}
