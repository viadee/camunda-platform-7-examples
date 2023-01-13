package de.viadee.bpm.camunda.engine.plugin;

import de.viadee.bpm.camunda.service.NotificationMapper;
import de.viadee.bpm.camunda.service.NotificationService;
import org.camunda.bpm.engine.impl.incident.IncidentHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class IncidentNotificationHandlerConfiguration {


    @Bean
    public IncidentNotificationHandler failedJobType(final NotificationMapper notificationMapper,
                                                     final List<NotificationService> subscriptions) {

        return new IncidentNotificationHandler(
                "failedJob",
                notificationMapper,
                subscriptions
        );
    }


    @Bean
    public IncidentNotificationHandler failedExternalTaskType(final NotificationMapper notificationMapper,
                                                              final List<NotificationService> subscriptions) {

        return new IncidentNotificationHandler(
                "failedExternalTask",
                notificationMapper,
                subscriptions
        );
    }


    @Bean
    public IncidentNotificationProcessEnginePlugin config(final List<IncidentHandler> incidentNotificationHandlers) {
        return new IncidentNotificationProcessEnginePlugin(incidentNotificationHandlers);
    }

}
