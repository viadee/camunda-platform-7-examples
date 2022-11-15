package de.viadee.bpm.camunda.externaltask.worker;

import de.viadee.bpm.camunda.processcontext.ProcessContext;
import de.viadee.bpm.camunda.service.ArchivService;
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
@ExternalTaskSubscription(topicName = "dokument-archivieren")
public class DokumentArchivierenHandler implements ExternalTaskHandler {

  private final ArchivService archivService;

  @Override
  public void execute(final ExternalTask task, final ExternalTaskService taskService) {
    var context = new ProcessContext(task);
    var dokument = context.getDokument();
    var archived = archivService.archive(dokument);
    log.info("{}", archived);

    taskService.complete(task, context.addArchivDokument(archived));
  }
}
