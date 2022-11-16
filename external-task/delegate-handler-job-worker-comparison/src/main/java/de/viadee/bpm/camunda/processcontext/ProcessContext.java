package de.viadee.bpm.camunda.processcontext;

import io.camunda.zeebe.client.api.response.ActivatedJob;
import model.Archived;
import model.DamageReport;
import model.Document;
import model.JsonDataType;
import model.Contract;
import org.camunda.bpm.client.task.ExternalTask;
import org.camunda.bpm.client.variable.ClientValues;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.VariableScope;
import org.camunda.bpm.engine.variable.VariableMap;
import org.camunda.spin.plugin.variable.SpinValues;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static de.viadee.bpm.camunda.processcontext.VariableConstants.EXT_IN_DAMAGE_REPORT;
import static de.viadee.bpm.camunda.processcontext.VariableConstants.EXT_IN_CONTRACT;
import static de.viadee.bpm.camunda.processcontext.VariableConstants.EXT_OUT_CLAIM_ID;
import static de.viadee.bpm.camunda.processcontext.VariableConstants.INT_ARCHIVED;
import static de.viadee.bpm.camunda.processcontext.VariableConstants.TEC_ITERATION_ELEMENT;
import static java.util.Objects.isNull;
import static model.JsonDataType.fromList;
import static model.JsonDataType.toJsonList;

public class ProcessContext {

  enum ContextType {DELEGATE, EXTERNAL, ZEEBE}

  private final ContextType type;
  private final VariableScope variableScope;
  private final VariableMap variableMap;
  private final ActivatedJob activatedJob;

  public ProcessContext(final DelegateExecution execution) {
    variableMap = null;
    variableScope = execution;
    activatedJob = null;
    type = ContextType.DELEGATE;
  }

  public ProcessContext(final ExternalTask task) {
    variableMap = task.getAllVariablesTyped();
    variableScope = null;
    activatedJob = null;
    type = ContextType.EXTERNAL;
  }

  public ProcessContext(final ActivatedJob job) {
    variableMap = null;
    variableScope = null;
    activatedJob = job;
    type = ContextType.ZEEBE;
  }

  public Map<String, Object> addArchivedDocument(final Archived archived) {
    switch (type) {
      case DELEGATE:
        if (isNull(variableScope.getVariable(INT_ARCHIVED))) {
          variableScope.setVariable(INT_ARCHIVED, toJsonList(new ArrayList<>(List.of(archived))));

        } else {
          var archivedList = fromList((List<String>) variableScope.getVariable(INT_ARCHIVED), Archived.class);
          archivedList.add(archived);
          variableScope.setVariable(INT_ARCHIVED, toJsonList(archivedList));
        }
        return null; // compatible with handler and job

        case EXTERNAL:
        if (isNull(variableMap.getValue(INT_ARCHIVED, List.class))) {
          return Map.of(INT_ARCHIVED, toJsonList(new ArrayList<>(List.of(archived))));

        } else {
          var archivedList = fromList(variableMap.getValue(INT_ARCHIVED, List.class), Archived.class);
          archivedList.add(archived);
          return Map.of(INT_ARCHIVED, toJsonList(archivedList));
        }

      case ZEEBE:
        // not needed
    }
    return null;
  }

  public DamageReport getDamageReport() {
    switch (type) {
      case DELEGATE:
        return JsonDataType.from(String.valueOf(variableScope.getVariable(EXT_IN_DAMAGE_REPORT)), DamageReport.class);
      case EXTERNAL:
        return JsonDataType.from(variableMap.getValue(EXT_IN_DAMAGE_REPORT, String.class), DamageReport.class);
      case ZEEBE:
        return JsonDataType.from(activatedJob.getVariablesAsMap().get(EXT_IN_DAMAGE_REPORT), DamageReport.class);
    }
    return null;
  }

  public Contract getContract() {
    switch (type) {
      case DELEGATE:
        return JsonDataType.from(String.valueOf(variableScope.getVariable(EXT_IN_CONTRACT)), Contract.class);
      case EXTERNAL:
        return JsonDataType.from(variableMap.getValue(EXT_IN_CONTRACT, String.class), Contract.class);
      case ZEEBE:
        return JsonDataType.from(activatedJob.getVariablesAsMap().get(EXT_IN_CONTRACT), Contract.class);
    }
    return null;
  }

  public Document getDocument() {
    switch (type) {
      case DELEGATE:
        return JsonDataType.from(String.valueOf(variableScope.getVariable(TEC_ITERATION_ELEMENT)), Document.class);
      case EXTERNAL:
        return JsonDataType.from(variableMap.getValue(TEC_ITERATION_ELEMENT, String.class), Document.class);
      case ZEEBE:
        return JsonDataType.from(activatedJob.getVariablesAsMap().get(TEC_ITERATION_ELEMENT), Document.class);
    }
    return null;
  }

  public Map<String, Object> setContract(final Contract contract) {
    switch (type) {
      case DELEGATE:
        variableScope.setVariable(EXT_IN_CONTRACT, SpinValues.jsonValue(contract.toJsonString()));
        return variableScope.getVariables(); // compatible with handler and job
      case EXTERNAL:
        return ClientValues.createVariables().putValue(EXT_IN_CONTRACT, contract.toJson());
      case ZEEBE:
        return Map.of(EXT_IN_CONTRACT, contract);
    }
    return null;
  }

  public Map<String, Object> setClaimId(final String claimId) {
    switch (type) {
      case DELEGATE:
        variableScope.setVariable(EXT_OUT_CLAIM_ID, claimId);
        return variableScope.getVariables(); // compatible with handler and job
      case EXTERNAL:
      case ZEEBE:
        return Map.of(EXT_OUT_CLAIM_ID, claimId);
    }
    return null;
  }

}
