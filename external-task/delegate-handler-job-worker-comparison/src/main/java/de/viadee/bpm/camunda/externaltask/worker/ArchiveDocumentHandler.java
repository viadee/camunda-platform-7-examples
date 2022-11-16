package de.viadee.bpm.camunda.externaltask.worker;

import de.viadee.bpm.camunda.processcontext.ProcessContext;
import de.viadee.bpm.camunda.service.ArchiveService;
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
@ExternalTaskSubscription(topicName = "archive-document")
public class ArchiveDocumentHandler implements ExternalTaskHandler {

  private final ArchiveService archiveService;

  @Override
  public void execute(final ExternalTask task,
                      final ExternalTaskService taskService) {

    var context = new ProcessContext(task);
    var archived = archiveService.archive(context.getDocument());
    log.info("{}", archived);

    taskService.complete(task, context.addArchivedDocument(archived));
  }
}
