package de.viadee.bpm.camunda.model;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.json.JsonMapper;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public interface JsonDataType {

    JsonMapper mapper = new JsonMapper();


    /**
     * Deserialize a given json-string to a given type using a {@link JsonMapper}
     *
     * @param json Object of type, serialized as json
     * @param type Type of object
     * @return Deserialized object
     * @param <T> Type used for deserialization
     */
    static <T> T from(final String json, final Class<T> type) {
        try {
            return mapper.readValue(json, type);

        } catch (JsonProcessingException e) {
            throw new JsonDataException("Could not deserialize object of type:" + type, e);
        }
    }


    /**
     * Deserialize a list of given json-strings to a @{@link List} of
     * objects for a given type using a {@link JsonMapper}
     *
     * @param jsons List of json-strings
     * @param type Type of objects
     * @return List of deserialized objects
     * @param <T> Type used for deserialization
     */
    static <T> List<T> fromList(final List<String> jsons, final Class<T> type) {
        return jsons.stream().map(elem -> JsonDataType.from(elem, type)).collect(Collectors.toList());
    }


    /**
     * Serialize a list of @{@link JsonDataType}-objects to a list of Strings (formatted as json each)
     *
     * @param objects Objects to serialize
     * @return @{@link List} of serialized objects
     */
    static List<String> toJsonList(final List<? extends JsonDataType> objects) {
        return objects.stream()
                      .filter(Objects::nonNull)
                      .map(JsonDataType::toJson).collect(Collectors.toList());
    }


    /**
     * Serialize a list of @{@link JsonDataType}-objects to a true json-list
     *
     * @param objects Objects to serialize
     * @return Json-list as String
     */
    static String toJsonString(final List<? extends JsonDataType> objects) {
        return "[" + String.join(",", toJsonList(objects)) + "]";
    }


    /**
     * Serialize a @{@link JsonDataType}-object to a json-string
     *
     * @return Json-representation of this as string
     */
    default String toJson() {
        try {
            return mapper.writeValueAsString(this);
        } catch (JsonProcessingException e) {
            throw new JsonDataException("Could not serialize value of type: " + this.getClass(), e);
        }
    }
}
