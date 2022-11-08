package de.viadee.bpm.camunda.externaltask.worker;

import de.viadee.bpm.camunda.model.JsonDataType;
import org.camunda.bpm.client.spring.annotation.ExternalTaskSubscription;
import org.camunda.bpm.client.task.ExternalTask;
import org.camunda.bpm.client.task.ExternalTaskHandler;
import org.camunda.bpm.client.task.ExternalTaskService;
import org.camunda.bpm.client.variable.ClientValues;
import org.camunda.bpm.client.variable.value.JsonValue;
import org.camunda.spin.Spin;
import org.camunda.spin.json.SpinJsonNode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import static org.camunda.spin.Spin.JSON;

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

        Spin.JSON( new Person("Alice", "Hallo world!")).toString();

        var jsonValues = persons.stream()
                                .map(Spin::JSON)
                                .map(SpinJsonNode::toString)
                                .map(ClientValues::jsonValue)
                                .collect(Collectors.toList());


        log.info("Data serialized: {}", jsonValues);

        log.info("Complete task\n");
        taskService.complete(task, ClientValues.createVariables()
                                               .putValue("Alice", persons.get(0))
                                               .putValue("Alice-Json", jsonValues.get(0))
                                               .putValue("persons", new ArrayList<>(jsonValues)));
    }

    static class Person {

        String name;
        String favouriteQuote;

        public Person(final String name, final String favouriteQuote) {
            this.name = name;
            this.favouriteQuote = favouriteQuote;
        }

        public String getName() {
            return name;
        }

        public void setName(final String name) {
            this.name = name;
        }

        public String getFavouriteQuote() {
            return favouriteQuote;
        }

        public void setFavouriteQuote(final String favouriteQuote) {
            this.favouriteQuote = favouriteQuote;
        }
    }
}
