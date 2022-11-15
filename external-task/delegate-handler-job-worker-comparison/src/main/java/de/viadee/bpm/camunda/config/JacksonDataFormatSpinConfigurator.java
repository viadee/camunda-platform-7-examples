package de.viadee.bpm.camunda.config;

import org.camunda.spin.impl.json.jackson.format.JacksonJsonDataFormat;
import org.camunda.spin.spi.DataFormatConfigurator;

public class JacksonDataFormatSpinConfigurator implements DataFormatConfigurator<JacksonJsonDataFormat> {

  public void configure(final JacksonJsonDataFormat dataFormat) {
    ObjectMapperConfig.configure(dataFormat.getObjectMapper());
  }

  public Class<JacksonJsonDataFormat> getDataFormatClass() {
    return JacksonJsonDataFormat.class;
  }
}
