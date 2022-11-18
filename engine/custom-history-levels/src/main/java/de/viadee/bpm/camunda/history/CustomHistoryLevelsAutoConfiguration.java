package de.viadee.bpm.camunda.history;

import org.camunda.bpm.engine.impl.history.HistoryLevel;
import org.camunda.bpm.engine.spring.SpringProcessEngineConfiguration;
import org.camunda.bpm.spring.boot.starter.configuration.impl.AbstractCamundaConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.ArrayList;
import java.util.Optional;

@Configuration
@EnableConfigurationProperties(HistoryProperties.class)
public class CustomHistoryLevelsAutoConfiguration {

    @Bean
    public AbstractCamundaConfiguration customHistoryLevelsConfiguration(final HistoryProperties properties) {
        return new AbstractCamundaConfiguration() {

            @Override
            public void preInit(final SpringProcessEngineConfiguration processEngineConfiguration) {
                var customHistoryLevels = new ArrayList<HistoryLevel>();
                Optional.ofNullable(processEngineConfiguration.getCustomHistoryLevels())
                        .ifPresent(customHistoryLevels::addAll);
                customHistoryLevels.add(new HistoryLevelFullWithVariableExclusion(properties));
                processEngineConfiguration.setCustomHistoryLevels(customHistoryLevels);
            }
        };
    }
}
