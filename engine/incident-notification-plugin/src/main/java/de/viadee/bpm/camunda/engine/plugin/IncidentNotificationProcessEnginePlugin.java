package de.viadee.bpm.camunda.engine.plugin;

import org.camunda.bpm.engine.ProcessEngine;
import org.camunda.bpm.engine.impl.cfg.ProcessEngineConfigurationImpl;
import org.camunda.bpm.engine.impl.cfg.ProcessEnginePlugin;
import org.camunda.bpm.engine.impl.incident.IncidentHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class IncidentNotificationProcessEnginePlugin implements ProcessEnginePlugin {
    private static final Logger log = LoggerFactory.getLogger(IncidentNotificationProcessEnginePlugin.class);

    private final List<IncidentHandler> incidentNotificationHandlers;

    public IncidentNotificationProcessEnginePlugin(final List<IncidentHandler> incidentNotificationHandlers) {
        this.incidentNotificationHandlers = incidentNotificationHandlers;
    }

    @Override
    public void preInit(final ProcessEngineConfigurationImpl processEngineConfiguration) {
        log.info("register incident-notification-handlers as custom-incident-handlers in process-engine-configuration");
        processEngineConfiguration.getCustomIncidentHandlers()
                                  .addAll(incidentNotificationHandlers);
    }

    @Override
    public void postInit(final ProcessEngineConfigurationImpl processEngineConfiguration) {
        // nop
    }

    @Override
    public void postProcessEngineBuild(final ProcessEngine processEngine) {
        // nop
    }
}
