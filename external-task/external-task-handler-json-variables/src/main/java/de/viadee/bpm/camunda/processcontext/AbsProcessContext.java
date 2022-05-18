package de.viadee.bpm.camunda.processcontext;

import de.viadee.bpm.camunda.model.JsonDataType;

import java.util.List;
import java.util.Map;

import static java.util.Objects.nonNull;

public abstract class AbsProcessContext {

    protected final Map<String, Object> variables;


    protected AbsProcessContext(final Map<String, Object> variables) {
        this.variables = variables;
    }


    <T extends JsonDataType> T readObject(final String variableName, final Class<T> type) {
        String json = this.readVariable(variableName);
        if (nonNull(json)) {
            return JsonDataType.from(json, type);

        } else {
            return null;
        }
    }

    <T extends JsonDataType> List<T> readList(final String variableName, final Class<T> type) {
        List<String> jsons = this.readVariable(variableName);
        if (nonNull(jsons)) {
            return JsonDataType.fromList(jsons, type);

        } else {
            return null;
        }
    }


    <T> T readVariable(final String variableName) {
        if (this.variables.containsKey(variableName)) {
            return (T) this.variables.get(variableName);

        } else {
            return null;
        }
    }
}
