package de.viadee.bpm.camunda.service;

import de.viadee.bpm.camunda.rest.api.client.MessageApi;
import de.viadee.bpm.camunda.rest.api.client.VersionApi;
import de.viadee.bpm.camunda.rest.api.model.CorrelationMessageDto;
import de.viadee.bpm.camunda.rest.api.model.VariableValueDto;
import de.viadee.bpm.camunda.rest.api.model.VersionDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

import java.util.Map;
import java.util.concurrent.TimeUnit;

@Configuration
@EnableScheduling
public class ApiService {
    private static final Logger log = LoggerFactory.getLogger(ApiService.class);

    private final VersionApi versionApi;
    private final MessageApi messageApi;

    public ApiService(final VersionApi versionApi, final MessageApi messageApi) {
        this.versionApi = versionApi;
        this.messageApi = messageApi;
    }

    @Scheduled(fixedDelay = 10, initialDelay = 15, timeUnit = TimeUnit.SECONDS)
    public void testApi() {
        log.info("Get Camunda version using Api-Client...");
        VersionDto version = versionApi.getRestAPIVersion();
        log.info("Response ok, Camunda version: {}", version.getVersion());
    }


    @Scheduled(fixedDelay = 999999, initialDelay = 10, timeUnit = TimeUnit.SECONDS)
    public void startProcessInstanceByApi() {
        VariableValueDto someVariable = new VariableValueDto();
        someVariable.setType("string");
        someVariable.setValue("bar");

        CorrelationMessageDto message = new CorrelationMessageDto();
        message.setMessageName("start-message");
        message.setResultEnabled(true);
        message.setProcessVariables(Map.of("foo", someVariable));

        var messageResult = messageApi.deliverMessage(message);
        log.info("Process started by message using rest-api ({})", messageResult.get(0).getProcessInstance().getId());
    }

}
