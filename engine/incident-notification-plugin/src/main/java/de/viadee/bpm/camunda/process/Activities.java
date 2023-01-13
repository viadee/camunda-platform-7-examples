package de.viadee.bpm.camunda.process;

import org.camunda.bpm.client.spring.annotation.ExternalTaskSubscription;
import org.camunda.bpm.client.task.ExternalTaskHandler;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class Activities {
    private static final Logger log = LoggerFactory.getLogger(Activities.class);

    @Bean
    public JavaDelegate troubleDelegate() {
        return execution -> {
            log.info("execute java-delegate");
            throw new RuntimeException("Hallo Delegate Error!");
        };
    }

    @Bean
    @ExternalTaskSubscription("trouble-topic")
    public ExternalTaskHandler troubleTopic() {
        return (task, taskService) -> {
            log.info("execute external-task");
            // just fail 3 times
            taskService.handleFailure(
                    task,
                    "Hallo External Error!",
                    null,
                    task.getRetries() == null ? 2 : task.getRetries() - 1,
                    10000L);
        };
    }
}
