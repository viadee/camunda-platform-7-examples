package model;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.json.JsonMapper;
import de.viadee.bpm.camunda.config.ObjectMapperConfig;
import org.camunda.bpm.client.variable.ClientValues;
import org.camunda.bpm.client.variable.value.JsonValue;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public interface JsonDataType {

  JsonMapper mapper = ObjectMapperConfig.jsonMapper();


  /**
   * Deserialize a given json-string to a given type using a {@link JsonMapper}
   *
   * @param source Object of type, serialized as json
   * @param type Type of object
   * @return Deserialized object
   * @param <T> Type used for deserialization
   */
  static <T> T from(final Object source, final Class<T> type) {
    try {
      if (source instanceof String) {
        return mapper.readValue(String.valueOf(source), type);

      } else {
        return mapper.convertValue(source, type);

      }
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
                  .map(JsonDataType::toJsonString).collect(Collectors.toList());
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
   * Serialize a @{@link JsonDataType}-object as json-string
   *
   * @return Json-representation of this as string
   */

  default String toJsonString() {
    try {
      return mapper.writeValueAsString(this);
    } catch (JsonProcessingException e) {
      throw new JsonDataException("Could not serialize value of type: " + this.getClass(), e);
    }
  }

  default JsonValue toJson() {
    return ClientValues.jsonValue(this.toJsonString());
  }
}
