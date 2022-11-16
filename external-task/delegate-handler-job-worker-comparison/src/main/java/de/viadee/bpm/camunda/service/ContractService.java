package de.viadee.bpm.camunda.service;

import model.Contract;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.util.Objects;

import static java.time.LocalDate.now;
import static org.apache.commons.lang3.StringUtils.leftPad;
import static org.apache.commons.lang3.StringUtils.substring;

@Service
public class ContractService {

  private final ContractRepository repository;

  public ContractService() {
    repository = new ContractRepository();
  }

  public Contract getContractById(final String id) {
    return repository.findById(id);
  }

  private static class ContractRepository {

    Contract findById(final String id) {
      if (StringUtils.contains(id, "42")) {
        throw new ContractNotFound(id);
      }
      var contract = new Contract();
      contract.setId(id);
      var pnr = leftPad(substring(String.valueOf(Objects.hash(id)), -5), 5, '0');
      contract.setPolicyHolder("PNR" + pnr);
      contract.setBegin(now().minusMonths(8));
      contract.setTarif("TF_1911");
      return contract;
    }
  }

  private static class ContractNotFound extends RuntimeException {
    public ContractNotFound(final String id) {
      super("Contract not found, Id=" + id);
    }
  }

}
