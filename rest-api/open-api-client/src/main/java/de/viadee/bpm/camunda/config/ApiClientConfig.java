package de.viadee.bpm.camunda.config;

import de.viadee.bpm.camunda.rest.api.ApiClient;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

@Configuration
@ConfigurationProperties(prefix = "de.viadee.bpm.camunda")
public class ApiClientConfig {

    private String restApiBasePath;

    @Bean
    public ApiClient apiClient() {
        var client = new ApiClient();
        client.setBasePath(restApiBasePath);
        return client;
    }

    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplateBuilder().build();
    }


    public String getRestApiBasePath() {
        return restApiBasePath;
    }

    public void setRestApiBasePath(final String restApiBasePath) {
        this.restApiBasePath = restApiBasePath;
    }
}
