package de.viadee.bpm.camunda;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class ExternalTaskWorkerProcessApplication {

    public static void main(String... args) {
        SpringApplication.run(ExternalTaskWorkerProcessApplication.class, args);
    }
}
