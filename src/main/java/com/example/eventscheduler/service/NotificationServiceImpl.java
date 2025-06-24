package com.example.eventscheduler.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

/**
 * Concrete implementation of {@link NotificationService}.
 * Simulates a delay to mimic network latency for an external service call.
 */
@Service
public class NotificationServiceImpl implements NotificationService {

    private static final Logger logger = LoggerFactory.getLogger(NotificationServiceImpl.class);

    @Override
    public void notifyUser(String message) {
        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            logger.warn("Notification service thread interrupted during delay.", e);
        }
        logger.info("Notification sent: {}", message);
    }
}
