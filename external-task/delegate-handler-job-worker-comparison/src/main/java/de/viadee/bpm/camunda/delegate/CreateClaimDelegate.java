package de.viadee.bpm.camunda.delegate;

import de.viadee.bpm.camunda.processcontext.ProcessContext;
import de.viadee.bpm.camunda.service.ClaimService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class CreateClaimDelegate implements JavaDelegate {

  private final ClaimService claimService;

  @Override
  public void execute(final DelegateExecution execution) {

    var context = new ProcessContext(execution);
    var damageReport = context.getDamageReport();
    var contract = context.getContract();

    var claimId = claimService.createClaim(damageReport, contract);
    log.info("Claim created, claimId: {}", claimId);

    context.setClaimId(claimId);
  }
}
