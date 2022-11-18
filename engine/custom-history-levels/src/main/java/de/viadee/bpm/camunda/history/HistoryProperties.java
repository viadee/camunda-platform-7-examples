package de.viadee.bpm.camunda.history;

import org.apache.commons.lang3.StringUtils;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.ArrayList;
import java.util.List;

import static java.util.Objects.isNull;

@ConfigurationProperties(prefix = "de.viadee.bpm.camunda.history")
public class HistoryProperties {

    private String exclusionPrefix;
    private List<String> excludedVariables;

    public String getExclusionPrefix() {
        if (StringUtils.isEmpty(exclusionPrefix)) {
            exclusionPrefix = "__$"; // if prefix not set, make sure that not every variable is excluded
        }
        return exclusionPrefix;
    }

    public void setExclusionPrefix(final String exclusionPrefix) {
        this.exclusionPrefix = exclusionPrefix;
    }

    public List<String> getExcludedVariables() {
        if (isNull(excludedVariables)) {
            excludedVariables = new ArrayList<>();
        }
        return excludedVariables;
    }

    public void setExcludedVariables(List<String> excludedVariables) {
        this.excludedVariables = excludedVariables;
    }
}
