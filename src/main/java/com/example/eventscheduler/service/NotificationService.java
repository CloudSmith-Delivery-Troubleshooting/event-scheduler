package com.example.eventscheduler.service;

/**
 * Interface for a notification service.
 * This simulates an external dependency (e.g., an email service, push notification)
 * that might have network delays or failures.
 */
public interface NotificationService {
    /**
     * Sends a notification with the given message.
     * @param message The message to send.
     */
    void notifyUser(String message);
}
