package de.viadee.bpm.camunda.config;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.json.JsonMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import java.text.SimpleDateFormat;

import static com.fasterxml.jackson.databind.DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES;
import static com.fasterxml.jackson.databind.SerializationFeature.FAIL_ON_EMPTY_BEANS;
import static com.fasterxml.jackson.databind.SerializationFeature.WRITE_DATES_AS_TIMESTAMPS;
import static com.fasterxml.jackson.databind.SerializationFeature.WRITE_DATES_WITH_ZONE_ID;

public abstract class ObjectMapperConfig {

  private ObjectMapperConfig() {
    // nop
  }

  static void configure(final ObjectMapper mapper) {
    mapper.registerModule(new JavaTimeModule());
    mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
    mapper.enable(JsonGenerator.Feature.WRITE_BIGDECIMAL_AS_PLAIN);
    mapper.disable(FAIL_ON_UNKNOWN_PROPERTIES);
    mapper.disable(FAIL_ON_EMPTY_BEANS);
    mapper.disable(WRITE_DATES_AS_TIMESTAMPS);
    mapper.disable(WRITE_DATES_WITH_ZONE_ID);
    mapper.setDateFormat(new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss"));
  }

  public static JsonMapper jsonMapper() {
    var jsonMapper = new JsonMapper();
    configure(jsonMapper);
    return jsonMapper;
  }
}
