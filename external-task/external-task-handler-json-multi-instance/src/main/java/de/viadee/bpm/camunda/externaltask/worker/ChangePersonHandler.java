package de.viadee.bpm.camunda.externaltask.worker;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.json.JsonMapper;
import de.viadee.bpm.camunda.model.Person;
import org.camunda.bpm.client.spring.annotation.ExternalTaskSubscription;
import org.camunda.bpm.client.task.ExternalTask;
import org.camunda.bpm.client.task.ExternalTaskHandler;
import org.camunda.bpm.client.task.ExternalTaskService;
import org.camunda.bpm.engine.variable.Variables;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.UUID;

import static de.viadee.bpm.camunda.externaltask.worker.WriteJsonValueListHandler.writeAsString;
import static org.camunda.bpm.client.variable.ClientValues.jsonValue;

@Component
@ExternalTaskSubscription(topicName = "change-person")
public class ChangePersonHandler implements ExternalTaskHandler {
    private static final Logger log = LoggerFactory.getLogger(ChangePersonHandler.class);

    @Override
    public void execute(ExternalTask task, ExternalTaskService taskService) {

        var person = readJson(task.getVariable("person"));

        log.info("name: {}, quote: {}", person.getName(), person.getQuote());

        person.setToken(UUID.randomUUID().toString().split("-")[0]);

        taskService.complete(task, Variables.createVariables().putValue("person", jsonValue(writeAsString(person))));

    }

    private Person readJson(final String data) {
        try {
            return new JsonMapper().readValue(data, Person.class);
        } catch (final JsonProcessingException e) {
            log.warn("Error while writing data as json: {}", e.getMessage(), e);
            throw new RuntimeException("Error reading data: " + e.getMessage(), e);
        }
    }

}
