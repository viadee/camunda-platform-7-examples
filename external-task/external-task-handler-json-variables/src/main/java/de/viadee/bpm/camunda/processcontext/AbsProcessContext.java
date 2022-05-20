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


    /**
     * Read an object from process-context stored as json,
     * deserialize and return as given type.
     *
     * @param variableName Name of variable in process-context
     * @param type Type of object
     * @return Deserialized object
     */
    <T extends JsonDataType> T readObject(final String variableName, final Class<T> type) {
        String json = this.readVariable(variableName);
        if (nonNull(json)) {
            return JsonDataType.from(json, type);

        } else {
            return null;
        }
    }


    /**
     * Read a list of objects from process-context stored as @{@link List} of strings,
     * deserialize each and return as @{@link List} of the given type.
     *
     * @param variableName Name of variable in process-context
     * @param type Type of objects
     * @return Deserialized list of objects
     */
    <T extends JsonDataType> List<T> readList(final String variableName, final Class<T> type) {
        List<String> jsons = this.readVariable(variableName);
        if (nonNull(jsons)) {
            return JsonDataType.fromList(jsons, type);

        } else {
            return null;
        }
    }


    private <T> T readVariable(final String variableName) {
        if (this.variables.containsKey(variableName)) {
            return (T) this.variables.get(variableName);

        } else {
            return null;
        }
    }
}
