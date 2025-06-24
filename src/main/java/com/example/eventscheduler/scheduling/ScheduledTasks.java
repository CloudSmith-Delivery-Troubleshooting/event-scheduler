package com.example.eventscheduler.scheduling;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.Instant;

/**
 * Component responsible for defining and executing scheduled tasks.
 * Uses Spring's @Scheduled annotation.
 * (Based on Spring's Getting Started guide for scheduling)
 */
@Component
public class ScheduledTasks {

    private static final Logger logger = LoggerFactory.getLogger(ScheduledTasks.class);

    /**
     * Reports the current time every 5 seconds.
     * fixedRate: Specifies the interval between method invocations, measured from the start time of each invocation.
     */
    @Scheduled(fixedRate = 5000)
    public void reportCurrentTime() {
        logger.info("Scheduled Task - Current time: {}", Instant.now());
    }

    // You can add more scheduled methods here, e.g., with cron expressions or fixedDelay
    // @Scheduled(cron = "0 0 10 * * ?") // Every day at 10 AM
    // public void dailyReport() {
    //    logger.info("Running daily report at {}", Instant.now());
    // }
}
