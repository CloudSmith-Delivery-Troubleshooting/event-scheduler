package com.example.eventscheduler.service;

import org.springframework.stereotype.Service;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * A service that manages a shared counter.
 * This simulates a shared mutable state that could potentially cause flaky tests
 * if not handled carefully (e.g., proper reset in tests, thread safety).
 */
@Service
public class SharedCounterService {
    private final AtomicInteger counter = new AtomicInteger(0);

    /**
     * Increments the counter and returns the new value.
     * @return The incremented counter value.
     */
    public int incrementAndGet() {
        return counter.incrementAndGet();
    }

    /**
     * Returns the current value of the counter.
     * @return The current counter value.
     */
    public int get() {
        return counter.get();
    }

    /**
     * Resets the counter to 0. Useful for test setup/teardown.
     */
    public void reset() {
        counter.set(0);
    }
}
