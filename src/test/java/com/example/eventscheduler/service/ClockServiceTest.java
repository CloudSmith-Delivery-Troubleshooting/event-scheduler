package com.example.eventscheduler.service;

import com.example.eventscheduler.util.SystemClock;
import org.junit.jupiter.api.Test;

import java.time.Clock;
import java.time.Instant;
import java.time.ZoneId;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

/**
 * Unit tests for {@link ClockService} and its underlying {@link SystemClock}.
 * Focuses on verifying that the clock abstraction correctly provides time,
 * especially when a fixed clock is used.
 */
class ClockServiceTest {

    @Test
    void now_shouldReturnCurrentInstantFromSystemClock() {
        // Use a real system clock for this test
        Clock systemDefaultClock = Clock.systemDefaultZone();
        SystemClock systemClock = new SystemClock(systemDefaultClock);
        ClockService clockService = new ClockService(systemClock);

        Instant beforeCall = Instant.now(systemDefaultClock);
        Instant nowResult = clockService.now();
        Instant afterCall = Instant.now(systemDefaultClock);

        // The result should be very close to the actual system time
        assertFalse(nowResult.isBefore(beforeCall));
        assertFalse(nowResult.isAfter(afterCall));
    }

    @Test
    void now_shouldReturnFixedInstant_whenFixedClockIsUsed() {
        // Given a fixed clock (simulating a frozen clock scenario)
        Instant fixedInstant = Instant.parse("2025-06-23T17:00:00Z");
        Clock fixedClock = Clock.fixed(fixedInstant, ZoneId.of("UTC"));
        SystemClock systemClock = new SystemClock(fixedClock); // SystemClock wraps the fixedClock
        ClockService clockService = new ClockService(systemClock); // ClockService uses the SystemClock

        // When now() is called multiple times
        Instant result1 = clockService.now();
        try {
            Thread.sleep(100); // Simulate some time passing
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        Instant result2 = clockService.now();

        // Then both results should be the exact fixed instant, demonstrating "frozen" time
        assertEquals(fixedInstant, result1);
        assertEquals(fixedInstant, result2);
        assertEquals(result1, result2); // Ensure consistency
    }

    @Test
    void getZone_shouldReturnCorrectZoneId() {
        ZoneId expectedZone = ZoneId.of("America/New_York");
        Clock zonedClock = Clock.system(expectedZone);
        SystemClock systemClock = new SystemClock(zonedClock);
        ClockService clockService = new ClockService(systemClock);

        assertEquals(expectedZone, systemClock.getZone()); // Direct SystemClock method
    }
}
