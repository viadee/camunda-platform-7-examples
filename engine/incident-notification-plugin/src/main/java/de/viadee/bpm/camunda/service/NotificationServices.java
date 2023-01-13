package de.viadee.bpm.camunda.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class NotificationServices {
    private static final Logger log = LoggerFactory.getLogger(NotificationServices.class);

    @Bean
    public NotificationService emailService() {
        return notification -> log.info("✉️ Notification by mail... {}", notification);
    }

    @Bean
    public NotificationService ticketSystem() {
        return notification -> log.info("\uD83C\uDFAB Notification to ticket-system... {}", notification);
    }
}
