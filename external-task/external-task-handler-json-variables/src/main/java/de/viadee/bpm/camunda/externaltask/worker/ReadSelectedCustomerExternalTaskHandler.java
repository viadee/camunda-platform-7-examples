package de.viadee.bpm.camunda.externaltask.worker;

import de.viadee.bpm.camunda.processcontext.ProcessContext;
import org.camunda.bpm.client.spring.annotation.ExternalTaskSubscription;
import org.camunda.bpm.client.task.ExternalTask;
import org.camunda.bpm.client.task.ExternalTaskHandler;
import org.camunda.bpm.client.task.ExternalTaskService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
@ExternalTaskSubscription(topicName = "read-selected-customer")
public class ReadSelectedCustomerExternalTaskHandler implements ExternalTaskHandler {
    private static final Logger log = LoggerFactory.getLogger(ReadSelectedCustomerExternalTaskHandler.class);

    @Override
    public void execute(ExternalTask task, ExternalTaskService taskService) {

        log.info("Instantiate process-context");
        var processContext = new ProcessContext(task.getAllVariables());

        log.info("Access data using method");
        var customer = processContext.getSelectedCustomer();

        log.info("Hello {}, you're selected and this is you as json: {}", customer.getName(), customer.toJson());

        log.info("Complete task\n");
        taskService.complete(task);
    }
}
