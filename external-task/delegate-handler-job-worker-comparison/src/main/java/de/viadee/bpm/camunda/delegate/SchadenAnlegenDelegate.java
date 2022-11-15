package de.viadee.bpm.camunda.delegate;

import de.viadee.bpm.camunda.processcontext.ProcessContext;
import de.viadee.bpm.camunda.service.SchadenService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class SchadenAnlegenDelegate implements JavaDelegate {

  private final SchadenService schadenService;

  @Override
  public void execute(final DelegateExecution execution) {

    var context = new ProcessContext(execution);
    var schadenmeldung = context.getSchadenmeldung();
    var vertrag = context.getVertrag();

    var schadenId = schadenService.schadenAnlegen(schadenmeldung, vertrag);
    log.info("Schaden erfolgreich angelegt, Schaden-Id: {}", schadenId);

    context.setSchadenId(schadenId);
  }
}
