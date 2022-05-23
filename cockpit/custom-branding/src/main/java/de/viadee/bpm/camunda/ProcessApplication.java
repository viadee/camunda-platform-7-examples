package de.viadee.bpm.camunda;

import de.viadee.bpm.camunda.branding.CustomBrandingProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@SpringBootApplication
@EnableConfigurationProperties({CustomBrandingProperties.class })
public class ProcessApplication {

    public static void main(String... args) {
        SpringApplication.run(ProcessApplication.class, args);
    }
}
