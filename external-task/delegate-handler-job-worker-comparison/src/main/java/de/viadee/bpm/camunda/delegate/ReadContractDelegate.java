package de.viadee.bpm.camunda.delegate;

import de.viadee.bpm.camunda.processcontext.ProcessContext;
import de.viadee.bpm.camunda.service.ContractService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class ReadContractDelegate implements JavaDelegate {

  private final ContractService contractService;

  public void execute(final DelegateExecution execution) {
    // read data
    var context = new ProcessContext(execution);
    var damageReport = context.getDamageReport();
    var vsnr = damageReport.getVsnr();

    // call service
    var contract = contractService.getContractById(vsnr);
    log.info("Contract found: {}", contract);

    // write data (auto-complete)
    context.setContract(contract);
  }
}
