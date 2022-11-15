package de.viadee.bpm.camunda.processcontext;

import io.camunda.zeebe.client.api.response.ActivatedJob;
import model.Archived;
import model.Dokument;
import model.JsonDataType;
import model.Schadenmeldung;
import model.Vertrag;
import org.camunda.bpm.client.task.ExternalTask;
import org.camunda.bpm.client.variable.ClientValues;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.VariableScope;
import org.camunda.bpm.engine.variable.VariableMap;
import org.camunda.spin.plugin.variable.SpinValues;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static de.viadee.bpm.camunda.processcontext.VariableConstants.EXT_IN_SCHADENMELDUNG;
import static de.viadee.bpm.camunda.processcontext.VariableConstants.EXT_IN_VERTRAG;
import static de.viadee.bpm.camunda.processcontext.VariableConstants.EXT_OUT_SCHADEN_ID;
import static de.viadee.bpm.camunda.processcontext.VariableConstants.INT_ARCHIVED;
import static de.viadee.bpm.camunda.processcontext.VariableConstants.TEC_ITERATION_ELEMENT;
import static java.util.Objects.isNull;

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

  public Map<String, Object> addArchivDokument(final Archived archived) {
    switch (type) {
      case DELEGATE:
        if (isNull(variableScope.getVariable(INT_ARCHIVED))) {
          variableScope.setVariable(INT_ARCHIVED, JsonDataType.toJsonList(new ArrayList<>(List.of(archived))));
        } else {
          var archivedList = JsonDataType.fromList((List<String>) variableScope.getVariable(INT_ARCHIVED), Archived.class);
          archivedList.add(archived);
          variableScope.setVariable(INT_ARCHIVED, JsonDataType.toJsonList(archivedList));
        }
        return null;

        case EXTERNAL:
        if (isNull(variableMap.getValue(INT_ARCHIVED, List.class))) {
          return Map.of(INT_ARCHIVED, JsonDataType.toJsonList(new ArrayList<>(List.of(archived))));

        } else {
          var archivedList = JsonDataType.fromList(variableMap.getValue(INT_ARCHIVED, List.class), Archived.class);
          archivedList.add(archived);
          return Map.of(INT_ARCHIVED, JsonDataType.toJsonList(archivedList));
        }

      case ZEEBE:
        // not needed
    }
    return null;
  }

  public Schadenmeldung getSchadenmeldung() {
    switch (type) {
      case DELEGATE:
        return JsonDataType.from(String.valueOf(variableScope.getVariable(EXT_IN_SCHADENMELDUNG)), Schadenmeldung.class);
      case EXTERNAL:
        return JsonDataType.from(variableMap.getValue(EXT_IN_SCHADENMELDUNG, String.class), Schadenmeldung.class);
      case ZEEBE:
        return JsonDataType.from(activatedJob.getVariablesAsMap().get(EXT_IN_SCHADENMELDUNG), Schadenmeldung.class);
    }
    return null;
  }

  public Vertrag getVertrag() {
    switch (type) {
      case DELEGATE:
        return JsonDataType.from(String.valueOf(variableScope.getVariable(EXT_IN_VERTRAG)), Vertrag.class);
      case EXTERNAL:
        return JsonDataType.from(variableMap.getValue(EXT_IN_VERTRAG, String.class), Vertrag.class);
      case ZEEBE:
        return JsonDataType.from(activatedJob.getVariablesAsMap().get(EXT_IN_VERTRAG), Vertrag.class);
    }
    return null;
  }

  public Dokument getDokument() {
    switch (type) {
      case DELEGATE:
        return JsonDataType.from(String.valueOf(variableScope.getVariable(TEC_ITERATION_ELEMENT)), Dokument.class);
      case EXTERNAL:
        return JsonDataType.from(variableMap.getValue(TEC_ITERATION_ELEMENT, String.class), Dokument.class);
      case ZEEBE:
        return JsonDataType.from(activatedJob.getVariablesAsMap().get(TEC_ITERATION_ELEMENT), Dokument.class);
    }
    return null;
  }

  public Map<String, Object> setVertrag(final Vertrag vertrag) {
    switch (type) {
      case DELEGATE:
        variableScope.setVariable(EXT_IN_VERTRAG, SpinValues.jsonValue(vertrag.toJsonString()));
        return variableScope.getVariables();
      case EXTERNAL:
        return ClientValues.createVariables().putValue(EXT_IN_VERTRAG, vertrag.toJson());
      case ZEEBE:
        return Map.of(EXT_IN_VERTRAG, vertrag);
    }
    return null;
  }

  public Map<String, Object> setSchadenId(final String schadenId) {
    switch (type) {
      case DELEGATE:
        variableScope.setVariable(EXT_OUT_SCHADEN_ID, schadenId);
        return variableScope.getVariables();
      case EXTERNAL:
      case ZEEBE:
        return Map.of(EXT_OUT_SCHADEN_ID, schadenId);
    }
    return null;
  }

}
