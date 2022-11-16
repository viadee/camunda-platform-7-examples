package de.viadee.bpm.camunda.delegate;

import de.viadee.bpm.camunda.processcontext.ProcessContext;
import de.viadee.bpm.camunda.service.ArchiveService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class ArchiveDocumentDelegate implements JavaDelegate {

  private final ArchiveService archiveService;

  @Override
  public void execute(final DelegateExecution execution) {

    var context = new ProcessContext(execution);
    var archived = archiveService.archive(context.getDocument());
    context.addArchivedDocument(archived);
    log.info("{}", archived);

  }
}
