package de.viadee.bpm.camunda.externaltask.worker;

import de.viadee.bpm.camunda.model.Address;
import de.viadee.bpm.camunda.model.Customer;
import de.viadee.bpm.camunda.model.JsonDataType;
import org.camunda.bpm.client.spring.annotation.ExternalTaskSubscription;
import org.camunda.bpm.client.task.ExternalTask;
import org.camunda.bpm.client.task.ExternalTaskHandler;
import org.camunda.bpm.client.task.ExternalTaskService;
import org.camunda.bpm.client.variable.ClientValues;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.List;

import static de.viadee.bpm.camunda.model.JsonDataType.toJsonString;
import static de.viadee.bpm.camunda.processcontext.ProcessContext.ALL_CUSTOMERS_JSON_KEY;
import static de.viadee.bpm.camunda.processcontext.ProcessContext.ALL_CUSTOMERS_LIST_KEY;
import static org.camunda.bpm.client.variable.ClientValues.jsonValue;

@Component
@ExternalTaskSubscription(topicName = "write-customers-to-json-list")
public class WriteAllCustomersToJsonListExternalTaskHandler implements ExternalTaskHandler {
    private static final Logger log = LoggerFactory.getLogger(WriteAllCustomersToJsonListExternalTaskHandler.class);

    @Override
    public void execute(ExternalTask task, ExternalTaskService taskService) {

        log.info("Create some data");
        var customers = List.of(
            new Customer("Alice", new Address("Madison Square Garden")),
            new Customer("Eve", null));

        log.info("Serialize {} objects of type <{}> as json", customers.size(), customers.get(0).getClass().getSimpleName());
        List<String> customersAsJsonStringList = JsonDataType.toJsonList(customers);

        log.info("Data serialized: {}", customersAsJsonStringList);

        log.info("Store as 'native' collection: '{}', key: '{}'", customersAsJsonStringList.getClass(), ALL_CUSTOMERS_LIST_KEY);
        var variables = ClientValues.createVariables()
                                    .putValue(ALL_CUSTOMERS_LIST_KEY, customersAsJsonStringList)
                                    .putValue(ALL_CUSTOMERS_JSON_KEY, jsonValue(toJsonString(customers)));

        log.info("Complete task\n");
        taskService.complete(task, variables);
    }
}
