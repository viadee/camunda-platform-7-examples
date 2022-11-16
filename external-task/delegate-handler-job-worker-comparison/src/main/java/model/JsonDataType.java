package model;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.json.JsonMapper;
import de.viadee.bpm.camunda.config.ObjectMapperConfig;
import org.camunda.bpm.client.variable.ClientValues;
import org.camunda.bpm.client.variable.value.JsonValue;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * Compare <a href="https://github.com/viadee/camunda-platform-7-examples/tree/main/external-task/external-task-handler-json-variables">
 *   external-task-handler-json-variables</a>
 */
public interface JsonDataType {

  JsonMapper mapper = ObjectMapperConfig.jsonMapper();


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


  static <T> List<T> fromList(final List<String> jsons, final Class<T> type) {
    return jsons.stream().map(elem -> JsonDataType.from(elem, type)).collect(Collectors.toList());
  }

  static List<String> toJsonList(final List<? extends JsonDataType> objects) {
    return objects.stream()
                  .filter(Objects::nonNull)
                  .map(JsonDataType::toJsonString).collect(Collectors.toList());
  }


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
