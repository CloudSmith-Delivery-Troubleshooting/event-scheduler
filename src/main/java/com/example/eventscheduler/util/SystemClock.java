package com.example.eventscheduler.util;

import java.time.Clock;
import java.time.Instant;
import java.time.ZoneId;

/**
 * A utility class that wraps Java's {@link Clock} to provide a consistent way
 * to retrieve the current time. This abstraction is crucial for testing
 * time-dependent logic, as the underlying Clock can be easily swapped
 * (e.g., with a fixed or advancing clock).
 */
public class SystemClock {

    private final Clock clock;

    /**
     * Constructs a SystemClock with a given Clock instance.
     * @param clock The Clock instance to be used for retrieving time.
     */
    public SystemClock(Clock clock) {
        this.clock = clock;
    }

    /**
     * Returns the current instant according to the wrapped clock.
     * @return The current Instant.
     */
    public Instant now() {
        return clock.instant();
    }

    /**
     * Returns the time zone of the wrapped clock.
     * @return The ZoneId of the clock.
     */
    public ZoneId getZone() {
        return clock.getZone();
    }
}
