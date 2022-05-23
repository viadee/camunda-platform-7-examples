package de.viadee.bpm.camunda.branding;

import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import java.io.ByteArrayInputStream;
import java.io.IOException;

import static de.viadee.bpm.camunda.branding.CustomBrandingConstants.CSS_PLACEHOLDER_BUTTON_COLOR;
import static de.viadee.bpm.camunda.branding.CustomBrandingConstants.CSS_PLACEHOLDER_TOP_COLOR;
import static de.viadee.bpm.camunda.branding.CustomBrandingConstants.REQUEST_PATH_LOGO;
import static de.viadee.bpm.camunda.branding.CustomBrandingConstants.REQUEST_PATH_USER_STYLES;
import static de.viadee.bpm.camunda.branding.CustomBrandingConstants.RESOURCE_PATH_CUSTOM_LOGO;
import static de.viadee.bpm.camunda.branding.CustomBrandingConstants.RESOURCE_PATH_CUSTOM_STYLES;
import static java.lang.Thread.currentThread;
import static java.nio.charset.StandardCharsets.UTF_8;
import static java.util.Objects.nonNull;

@Component
@ConditionalOnProperty(value = "de.viadee.bpm.camunda.branding.enabled", matchIfMissing = true)
public class CustomBrandingResourceFilter implements Filter {
    private static final Logger log = LoggerFactory.getLogger(CustomBrandingResourceFilter.class);

    // preloaded resources after app start
    private static byte[] cssResource;
    private static byte[] logoResource;

    private final CustomBrandingProperties customBrandingProperties;

    public CustomBrandingResourceFilter(final CustomBrandingProperties customBrandingProperties) {
        this.customBrandingProperties = customBrandingProperties;
    }


    @Override
    public void doFilter(final ServletRequest servletRequest,
                         final ServletResponse response,
                         final FilterChain filterChain) throws IOException, ServletException {

        var request = (HttpServletRequest) servletRequest;

        // custom styles
        // if the request points to '/styles/user-styles.css', then replace resource by prepared custom-styles
        if (request.getRequestURI().endsWith(REQUEST_PATH_USER_STYLES) && nonNull(cssResource)) {
            IOUtils.copy(new ByteArrayInputStream(cssResource), response.getOutputStream());
            response.setContentType("text/css");
            return;
        }

        // custom logo
        // if the request points to '/custom-logo.png', then replace resource by prepared logo
        if (request.getRequestURI().endsWith(REQUEST_PATH_LOGO) && nonNull(logoResource)) {
            IOUtils.copy(new ByteArrayInputStream(logoResource), response.getOutputStream());
            response.setContentType("image/png");
            return;
        }

        filterChain.doFilter(request, response);
    }


    /**
     * Load resources just once during startup
     */
    @PostConstruct
    protected void preloadResources() {
        log.info("pre-load resources for platform branding");

        try {
            preloadLogo();
            preloadCss(customBrandingProperties.getButtonColor(), customBrandingProperties.getTopColor());

        } catch (final Exception exception) {
            log.error("Error occurred during pre-loading resources, app will start anyway", exception);
        }
    }

    @SuppressWarnings("ConstantConditions")
    private static void preloadLogo() throws IOException {
        logoResource = IOUtils.toByteArray(currentThread().getContextClassLoader().getResource(RESOURCE_PATH_CUSTOM_LOGO));
    }

    @SuppressWarnings("ConstantConditions")
    private static void preloadCss(final String buttonColor, final String topColor) throws IOException {
        var viadeeStyles = IOUtils.toString(currentThread().getContextClassLoader().getResource(RESOURCE_PATH_CUSTOM_STYLES), UTF_8);
        cssResource = viadeeStyles.replace(CSS_PLACEHOLDER_BUTTON_COLOR, buttonColor)
                                  .replace(CSS_PLACEHOLDER_TOP_COLOR, topColor)
                                  .getBytes();
    }
}
