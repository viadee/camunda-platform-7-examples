package de.viadee.bpm.camunda.config;

import org.camunda.bpm.client.spi.DataFormatConfigurator;
import org.camunda.bpm.client.variable.impl.format.json.JacksonJsonDataFormat;

public class JacksonDataFormatClientConfigurator implements DataFormatConfigurator<JacksonJsonDataFormat> {

  public void configure(final JacksonJsonDataFormat dataFormat) {
    ObjectMapperConfig.configure(dataFormat.getObjectMapper());
  }

  public Class<JacksonJsonDataFormat> getDataFormatClass() {
    return JacksonJsonDataFormat.class;
  }
}
