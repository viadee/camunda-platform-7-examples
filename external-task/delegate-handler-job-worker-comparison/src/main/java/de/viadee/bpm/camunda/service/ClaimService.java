package de.viadee.bpm.camunda.service;

import model.Contract;
import model.DamageReport;
import org.springframework.stereotype.Service;

@Service
public class ClaimService {

  public String createClaim(final DamageReport damageReport,
                            final Contract contract) {
    return
      "VN" + contract.getId()
        + "_" + contract.getPolicyHolder() + "_SN"
        + String.valueOf(damageReport.hashCode()).replace("-", "");
  }

}
