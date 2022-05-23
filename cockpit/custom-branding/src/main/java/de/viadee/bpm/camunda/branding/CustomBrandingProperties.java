package de.viadee.bpm.camunda.branding;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "de.viadee.bpm.camunda-branding")
public class CustomBrandingProperties {
    private boolean enabled = true;
    private String topColor = "#681885"; // default
    private String buttonColor = "#7EBCA9"; // default

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(final boolean enabled) {
        this.enabled = enabled;
    }

    public String getTopColor() {
        return topColor;
    }

    public void setTopColor(final String topColor) {
        this.topColor = topColor;
    }

    public String getButtonColor() {
        return buttonColor;
    }

    public void setButtonColor(final String buttonColor) {
        this.buttonColor = buttonColor;
    }

}
