package de.viadee.bpm.camunda.externaltask.worker;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.json.JsonMapper;
import de.viadee.bpm.camunda.model.Person;
import org.camunda.bpm.client.spring.annotation.ExternalTaskSubscription;
import org.camunda.bpm.client.task.ExternalTask;
import org.camunda.bpm.client.task.ExternalTaskHandler;
import org.camunda.bpm.client.task.ExternalTaskService;
import org.camunda.bpm.client.variable.ClientValues;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.List;

import static org.camunda.bpm.client.variable.ClientValues.jsonValue;

@Component
@ExternalTaskSubscription(topicName = "write-json-value-list")
public class WriteJsonValueListHandler implements ExternalTaskHandler {
    private static final Logger log = LoggerFactory.getLogger(WriteJsonValueListHandler.class);

    @Override
    public void execute(ExternalTask task, ExternalTaskService taskService) {

        log.info("Create some people");
        var persons = List.of(
            new Person("Alice", "Hallo world!"),
            new Person("Eve", "You know nothing!"),
            new Person("Bob", ""));

        log.info("Complete task\n");
        taskService.complete(task, ClientValues.createVariables()
                                               .putValue("persons", jsonValue(writeAsString(persons))));
    }

    public static  <T> String writeAsString(final T data) {
        try {
            return new JsonMapper().writeValueAsString(data);
        } catch (JsonProcessingException e) {
            log.warn("Error while writing data as json: {}", e.getMessage(), e);
            return "{\"error\":\"" + e.getMessage() + "\"}";
        }
    }

}
