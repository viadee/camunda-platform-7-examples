package de.viadee.bpm.camunda.zeebe;

import de.viadee.bpm.camunda.processcontext.ProcessContext;
import de.viadee.bpm.camunda.service.ClaimService;
import io.camunda.zeebe.client.api.response.ActivatedJob;
import io.camunda.zeebe.spring.client.annotation.JobWorker;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Map;

import static de.viadee.bpm.camunda.processcontext.VariableConstants.EXT_OUT_CLAIM_ID;

@Slf4j
@Component
@RequiredArgsConstructor
public class CreateClaimJob {

  private final ClaimService claimService;

  @JobWorker(type = "create-claim")
  public Map<String, Object> createClaim(final ActivatedJob job) {

    var context = new ProcessContext(job);
    var damageReport = context.getDamageReport();
    var contract = context.getContract();

    var claimId = claimService.createClaim(damageReport, contract);
    log.info("Claim created, claimId: {}", claimId);

    return Map.of(EXT_OUT_CLAIM_ID, claimId);
  }

}
