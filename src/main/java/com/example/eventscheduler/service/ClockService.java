package com.example.eventscheduler.service;

import com.example.eventscheduler.util.SystemClock;
import org.springframework.stereotype.Service;

import java.time.Instant;

/**
 * A service that provides access to the current time through the {@link SystemClock}.
 * This service acts as an additional layer of abstraction for time-related operations.
 */
@Service
public class ClockService {

    private final SystemClock systemClock;

    /**
     * Constructs a ClockService with a given SystemClock instance.
     * @param systemClock The SystemClock to be used for retrieving time.
     */
    public ClockService(SystemClock systemClock) {
        this.systemClock = systemClock;
    }

    /**
     * Returns the current instant according to the underlying clock.
     * @return The current Instant.
     */
    public Instant now() {
        return systemClock.now();
    }
}
