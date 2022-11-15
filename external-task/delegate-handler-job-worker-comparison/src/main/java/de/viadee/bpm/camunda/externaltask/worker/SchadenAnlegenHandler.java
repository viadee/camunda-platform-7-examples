package de.viadee.bpm.camunda.externaltask.worker;

import de.viadee.bpm.camunda.processcontext.ProcessContext;
import de.viadee.bpm.camunda.service.SchadenService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.camunda.bpm.client.spring.annotation.ExternalTaskSubscription;
import org.camunda.bpm.client.task.ExternalTask;
import org.camunda.bpm.client.task.ExternalTaskHandler;
import org.camunda.bpm.client.task.ExternalTaskService;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
@ExternalTaskSubscription(topicName = "schaden-anlegen")
public class SchadenAnlegenHandler implements ExternalTaskHandler {

  private final SchadenService schadenService;

  @Override
  public void execute(final ExternalTask task,
                      final ExternalTaskService taskService) {

    var context = new ProcessContext(task);
    var schadenmeldung = context.getSchadenmeldung();
    var vertrag = context.getVertrag();

    var schadenId = schadenService.schadenAnlegen(schadenmeldung, vertrag);
    log.info("Schaden erfolgreich angelegt, Schaden-Id: {}", schadenId);

    taskService.complete(task, context.setSchadenId(schadenId));
  }
}
