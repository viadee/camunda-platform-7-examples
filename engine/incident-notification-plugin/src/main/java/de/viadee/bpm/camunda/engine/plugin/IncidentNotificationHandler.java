package de.viadee.bpm.camunda.engine.plugin;

import de.viadee.bpm.camunda.service.NotificationMapper;
import de.viadee.bpm.camunda.service.NotificationService;
import org.camunda.bpm.engine.impl.incident.DefaultIncidentHandler;
import org.camunda.bpm.engine.impl.incident.IncidentContext;
import org.camunda.bpm.engine.runtime.Incident;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class IncidentNotificationHandler extends DefaultIncidentHandler {
    private static final Logger log = LoggerFactory.getLogger(IncidentNotificationHandler.class);

    private final NotificationMapper notificationMapper;
    private final List<NotificationService> subscriptions;

    public IncidentNotificationHandler(final String type,
                                       final NotificationMapper notificationMapper,
                                       final List<NotificationService> subscriptions) {
        super(type);
        this.notificationMapper = notificationMapper;
        this.subscriptions = subscriptions;
    }

    @Override
    public Incident handleIncident(final IncidentContext incidentContext, final String message) {
        log.error("\uD83D\uDCA3 Incident: {}", message);

        var incident = super.handleIncident(incidentContext, message);
        var notification = notificationMapper.map(incident);
        subscriptions.forEach(s -> s.publish(notification));

        return incident;
    }

}
