package com.example.eventscheduler.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for {@link SharedCounterService}.
 * Ensures the counter's behavior (increment, get, reset) is correct and thread-safe.
 */
class SharedCounterServiceTest {

    private SharedCounterService sharedCounterService;

    @BeforeEach
    void setUp() {
        // Initialize and reset the counter before each test to ensure isolated tests
        sharedCounterService = new SharedCounterService();
        sharedCounterService.reset();
    }

    @Test
    void incrementAndGet_shouldIncreaseCounterValue() {
        assertEquals(1, sharedCounterService.incrementAndGet());
        assertEquals(2, sharedCounterService.incrementAndGet());
        assertEquals(3, sharedCounterService.incrementAndGet());
    }

    @Test
    void get_shouldReturnCurrentCounterValue() {
        sharedCounterService.incrementAndGet(); // Counter is now 1
        assertEquals(1, sharedCounterService.get());
        sharedCounterService.incrementAndGet(); // Counter is now 2
        assertEquals(2, sharedCounterService.get());
    }

    @Test
    void reset_shouldSetCounterToZero() {
        sharedCounterService.incrementAndGet(); // Counter is 1
        sharedCounterService.reset();
        assertEquals(0, sharedCounterService.get());

        sharedCounterService.incrementAndGet(); // Counter is 1 again
        assertEquals(1, sharedCounterService.get());
    }

    @Test
    void concurrentIncrementAndGet_shouldMaintainAccuracy() throws InterruptedException {
        // Test for thread safety in a basic way
        int numThreads = 10;
        int incrementsPerThread = 1000;
        Thread[] threads = new Thread[numThreads];

        for (int i = 0; i < numThreads; i++) {
            threads[i] = new Thread(() -> {
                for (int j = 0; j < incrementsPerThread; j++) {
                    sharedCounterService.incrementAndGet();
                }
            });
            threads[i].start();
        }

        for (int i = 0; i < numThreads; i++) {
            threads[i].join(); // Wait for all threads to complete
        }

        assertEquals(numThreads * incrementsPerThread, sharedCounterService.get());
    }
}
