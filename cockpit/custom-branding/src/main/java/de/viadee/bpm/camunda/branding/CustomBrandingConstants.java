package de.viadee.bpm.camunda.branding;

public final class CustomBrandingConstants {

    // request paths for filtering original resources
    static final String REQUEST_PATH_USER_STYLES = "/styles/user-styles.css";
    static final String REQUEST_PATH_LOGO = "/custom-logo.png";

    // placeholders in css to be replaced once during startup
    static final String CSS_PLACEHOLDER_BUTTON_COLOR = "${custom-button-color}";
    static final String CSS_PLACEHOLDER_TOP_COLOR = "${custom-top-color}";

    // resource names to be used as replacement when original resources are requested
    static final String RESOURCE_PATH_CUSTOM_STYLES = "branding/custom-styles.css";
    static final String RESOURCE_PATH_CUSTOM_LOGO = "branding/custom-logo.png";

}
