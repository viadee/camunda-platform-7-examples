package de.viadee.bpm.camunda.delegate;

import de.viadee.bpm.camunda.processcontext.ProcessContext;
import de.viadee.bpm.camunda.service.VertragService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class VertragLesenDelegate implements JavaDelegate {

  private final VertragService vertragService;

  public void execute(final DelegateExecution execution) {
    // read data
    var context = new ProcessContext(execution);
    var schadenmeldung = context.getSchadenmeldung();
    var vsnr = schadenmeldung.getVsnr();

    // call service
    var vertrag = vertragService.getVertragById(vsnr);
    log.info("Vertrag erfolgreich gelesen: {}", vertrag);

    // write data (auto-complete)
    context.setVertrag(vertrag);
  }
}
