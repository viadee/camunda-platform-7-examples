package de.viadee.bpm.camunda.zeebe;

import de.viadee.bpm.camunda.processcontext.ProcessContext;
import de.viadee.bpm.camunda.service.ContractService;
import io.camunda.zeebe.client.api.response.ActivatedJob;
import io.camunda.zeebe.spring.client.annotation.JobWorker;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Map;

@Slf4j
@Component
@RequiredArgsConstructor
public class ReadContractJob {

  private final ContractService contractService;

  @JobWorker // type: "set to empty string which leads to method name being used"
  public Map<String, Object> readContract(final ActivatedJob job) {

    // read data
    var context = new ProcessContext(job);
    var damageReport = context.getDamageReport();
    var vsnr = damageReport.getVsnr();

    // call service
    var contract = contractService.getContractById(vsnr);
    log.info("Contract found: {}", contract);

    // write data & auto-complete (default)
    return context.setContract(contract);
  }
}
