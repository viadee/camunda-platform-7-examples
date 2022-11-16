package de.viadee.bpm.camunda.externaltask.worker;

import de.viadee.bpm.camunda.processcontext.ProcessContext;
import de.viadee.bpm.camunda.service.ContractService;
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
@ExternalTaskSubscription("read-contract")
public class ReadContractHandler implements ExternalTaskHandler {

  private final ContractService contractService;

  public void execute(final ExternalTask task,
                      final ExternalTaskService taskService) {
    // read data
    var context = new ProcessContext(task);
    var damageReport = context.getDamageReport();
    var vsnr = damageReport.getVsnr();

    // call service
    var contract = contractService.getContractById(vsnr);
    log.info("Contract found: {}", contract);

    // complete task & write data
    taskService.complete(task, context.setContract(contract));
  }
}
