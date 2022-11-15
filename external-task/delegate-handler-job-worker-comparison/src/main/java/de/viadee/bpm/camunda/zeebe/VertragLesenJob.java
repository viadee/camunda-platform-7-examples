package de.viadee.bpm.camunda.zeebe;

import de.viadee.bpm.camunda.processcontext.ProcessContext;
import de.viadee.bpm.camunda.service.VertragService;
import io.camunda.zeebe.client.api.response.ActivatedJob;
import io.camunda.zeebe.spring.client.annotation.JobWorker;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Map;

@Slf4j
@Component
@RequiredArgsConstructor
public class VertragLesenJob {

  private final VertragService vertragService;

  @JobWorker
  public Map<String, Object> vertragLesen(final ActivatedJob job) {

    // read data
    var context = new ProcessContext(job);
    var schadenmeldung = context.getSchadenmeldung();
    var vsnr = schadenmeldung.getVsnr();

    // call service
    var vertrag = vertragService.getVertragById(vsnr);
    log.info("Vertrag erfolgreich gelesen: {}", vertrag);

    // write data & auto-complete (default)
    return context.setVertrag(vertrag);
  }
}
