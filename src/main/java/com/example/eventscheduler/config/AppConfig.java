package com.example.eventscheduler.config;

import com.example.eventscheduler.util.SystemClock;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.Clock;

/**
 * Spring configuration class to define application-wide beans.
 * Provides a configurable Clock instance for time-dependent logic.
 */
@Configuration
public class AppConfig {

    /**
     * Provides a standard Java Clock instance set to UTC system time.
     * This bean can be overridden in test configurations to provide fixed or mocked time.
     * @return A Clock instance.
     */
    @Bean
    public Clock clock() {
        return Clock.systemUTC();
    }

    /**
     * Provides a SystemClock bean that wraps the standard Java Clock.
     * This allows for a consistent way to access the current time throughout the application.
     * @param clock The Clock bean provided by Spring.
     * @return A SystemClock instance.
     */
    @Bean
    public SystemClock systemClock(Clock clock) {
        return new SystemClock(clock);
    }
}
