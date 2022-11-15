package de.viadee.bpm.camunda.externaltask.worker;

import de.viadee.bpm.camunda.processcontext.ProcessContext;
import de.viadee.bpm.camunda.service.VertragService;
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
@ExternalTaskSubscription("vertrag-lesen")
public class VertragLesenHandler implements ExternalTaskHandler {

  private final VertragService vertragService;

  public void execute(final ExternalTask task,
                      final ExternalTaskService taskService) {
    // read data
    var context = new ProcessContext(task);
    var schadenmeldung = context.getSchadenmeldung();
    var vsnr = schadenmeldung.getVsnr();

    // call service
    var vertrag = vertragService.getVertragById(vsnr);
    log.info("Vertrag erfolgreich gelesen: {}", vertrag);

    // complete task & write data
    taskService.complete(task, context.setVertrag(vertrag));
  }
}
