package de.viadee.bpm.camunda.history;

import org.camunda.bpm.engine.impl.history.HistoryLevelFull;
import org.camunda.bpm.engine.impl.history.event.HistoryEventType;
import org.camunda.bpm.engine.impl.history.event.HistoryEventTypes;
import org.camunda.bpm.engine.runtime.VariableInstance;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;
import java.util.List;

import static java.util.Objects.isNull;

public class HistoryLevelFullWithVariableExclusion extends HistoryLevelFull {
    private static final Logger log = LoggerFactory.getLogger("custom-history-level-demo");

    private static final List<HistoryEventType> VARIABLE_EVENT_TYPES = Arrays.asList(
            HistoryEventTypes.VARIABLE_INSTANCE_CREATE,
            HistoryEventTypes.VARIABLE_INSTANCE_DELETE,
            HistoryEventTypes.VARIABLE_INSTANCE_UPDATE,
            HistoryEventTypes.VARIABLE_INSTANCE_MIGRATE,
            HistoryEventTypes.VARIABLE_INSTANCE_UPDATE_DETAIL
    );

    private final String excludedPrefix;
    private final List<String> excludedVariableNames;

    public HistoryLevelFullWithVariableExclusion(final HistoryProperties properties) {
        this.excludedPrefix = properties.getExclusionPrefix();
        this.excludedVariableNames = properties.getExcludedVariables();
    }

    @Override
    public int getId() {
        return super.getId() + 10;
    }

    @Override
    public String getName() {
        return super.getName() + "-with-variable-exclusion";
    }

    @Override
    public boolean isHistoryEventProduced(HistoryEventType eventType, Object entity) {
        if (isNull(entity) || !(entity instanceof VariableInstance)) {
            return super.isHistoryEventProduced(eventType, entity);
        }
        var variable = (VariableInstance) entity;
        var isHistoryEventProduced = !(VARIABLE_EVENT_TYPES.contains(eventType) && isVariableExcluded(variable));
        log.info("HistoryEvent: {} for variable: '{}', isHistoryEventProduced: {} ", eventType, variable.getName(), isHistoryEventProduced);
        return isHistoryEventProduced;
    }

    private boolean isVariableExcluded(VariableInstance instance) {
        return excludedVariableNames.contains(instance.getName()) ||
          instance.getName().startsWith(excludedPrefix);
    }
}
