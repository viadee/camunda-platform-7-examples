package de.viadee.bpm.camunda.externaltask.worker;

import de.viadee.bpm.camunda.processcontext.ProcessContext;
import de.viadee.bpm.camunda.service.ClaimService;
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
@ExternalTaskSubscription(topicName = "create-claim")
public class CreateClaimHandler implements ExternalTaskHandler {

  private final ClaimService claimService;

  @Override
  public void execute(final ExternalTask task,
                      final ExternalTaskService taskService) {

    var context = new ProcessContext(task);
    var damageReport = context.getDamageReport();
    var contract = context.getContract();

    var claimId = claimService.createClaim(damageReport, contract);
    log.info("Claim created, claimId: {}", claimId);

    taskService.complete(task, context.setClaimId(claimId));
  }
}
