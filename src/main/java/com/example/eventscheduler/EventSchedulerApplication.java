package com.example.eventscheduler;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * Main application class for the Event Scheduler.
 * Enables Spring Boot features and scheduled task execution.
 */
@SpringBootApplication // Convenience annotation: @Configuration, @EnableAutoConfiguration, @ComponentScan
@EnableScheduling      // Enables Spring's scheduled task execution capability
public class EventSchedulerApplication {
    public static void main(String[] args) {
        SpringApplication.run(EventSchedulerApplication.class, args);
    }
}
