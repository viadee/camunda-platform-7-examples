package de.viadee.bpm.camunda.model;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.json.JsonMapper;

import java.util.List;
import java.util.stream.Collectors;

public interface JsonDataType {

    JsonMapper mapper = new JsonMapper();


    static <T> T from(final String json, final Class<T> type) {
        try {
            return mapper.readValue(json, type);

        } catch (JsonProcessingException e) {
            throw new JsonDataException("Could not deserialize object of type:" + type, e);
        }
    }


    static <T> List<T> fromList(final List<String> jsons, final Class<T> type) {
        return jsons.stream().map(elem -> JsonDataType.from(elem, type)).collect(Collectors.toList());
    }


    static List<String> toJsonList(final List<? extends JsonDataType> objects) {
        return objects.stream().map(JsonDataType::toJson).collect(Collectors.toList());
    }


    default String toJson() {
        try {
            return mapper.writeValueAsString(this);
        } catch (JsonProcessingException e) {
            throw new JsonDataException("Could not serialize value of type: " + this.getClass(), e);
        }
    }
}
