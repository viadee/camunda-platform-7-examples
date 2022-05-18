package de.viadee.bpm.camunda.externaltask.worker;

import de.viadee.bpm.camunda.model.Customer;
import de.viadee.bpm.camunda.processcontext.ProcessContext;
import org.apache.commons.lang3.RandomUtils;
import org.camunda.bpm.client.spring.annotation.ExternalTaskSubscription;
import org.camunda.bpm.client.task.ExternalTask;
import org.camunda.bpm.client.task.ExternalTaskHandler;
import org.camunda.bpm.client.task.ExternalTaskService;
import org.camunda.bpm.client.variable.ClientValues;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;

import static de.viadee.bpm.camunda.processcontext.ProcessContext.SELECTED_CUSTOMER_KEY;

@Component
@ExternalTaskSubscription(topicName = "select-one-customer")
public class SelectOneCustomerExternalTaskHandler implements ExternalTaskHandler {
    private static final Logger log = LoggerFactory.getLogger(SelectOneCustomerExternalTaskHandler.class);

    @Override
    public void execute(ExternalTask task, ExternalTaskService taskService) {

        log.info("Instantiate process-context");
        var processContext = new ProcessContext(task.getAllVariables());

        log.info("Access data using method");
        var customers = processContext.getAllCustomers();

        log.info("Hello {}!", customers.stream().map(Customer::getName).collect(Collectors.joining(" and ")));

        var selected = customers.get(RandomUtils.nextInt(0, customers.size()));
        log.info("Selected customer: '{}'", selected.getName());

        var selectedAsJson = selected.toJson();
        log.info("Selected customer as json: {}", selectedAsJson);

        log.info("Store as json-value, key: '{}'", SELECTED_CUSTOMER_KEY);
        var variables = ClientValues.createVariables()
                                    .putValue(SELECTED_CUSTOMER_KEY, ClientValues.jsonValue(selectedAsJson));

        log.info("Complete task\n");
        taskService.complete(task, variables);
    }
}
