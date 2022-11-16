package de.viadee.bpm.camunda.zeebe;

import de.viadee.bpm.camunda.processcontext.ProcessContext;
import de.viadee.bpm.camunda.service.ArchiveService;
import io.camunda.zeebe.client.api.response.ActivatedJob;
import io.camunda.zeebe.spring.client.annotation.JobWorker;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Map;

import static de.viadee.bpm.camunda.processcontext.VariableConstants.TEC_ITERATION_ELEMENT;

@Slf4j
@Component
@RequiredArgsConstructor
public class ArchiveDocumentJob {

  private final ArchiveService archiveService;

  @JobWorker(type = "archive-document")
  public Map<String, Object> archiveDocument(final ActivatedJob job) {

    var context = new ProcessContext(job);
    var archived = archiveService.archive(context.getDocument());
    log.info("{}", archived);

    return Map.of(TEC_ITERATION_ELEMENT, archived); // overwrite element locally
  }


}
