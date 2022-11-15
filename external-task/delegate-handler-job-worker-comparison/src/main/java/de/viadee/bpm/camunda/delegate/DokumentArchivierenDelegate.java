package de.viadee.bpm.camunda.delegate;

import de.viadee.bpm.camunda.processcontext.ProcessContext;
import de.viadee.bpm.camunda.service.ArchivService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class DokumentArchivierenDelegate implements JavaDelegate {

  private final ArchivService archivService;

  @Override
  public void execute(final DelegateExecution execution) {
    var context = new ProcessContext(execution);
    var dokument = context.getDokument();
    var archived = archivService.archive(dokument);
    context.addArchivDokument(archived);
    log.info("{}", archived);
  }
}
