package com.example.eventscheduler.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Custom exception to be thrown when an Event is not found.
 * Mapped to HTTP 404 Not Found by Spring's @ResponseStatus.
 */
@ResponseStatus(HttpStatus.NOT_FOUND)
public class EventNotFoundException extends RuntimeException {
    public EventNotFoundException(Long eventId) {
        super("Event not found with id: " + eventId);
    }
}
