package com.example.eventscheduler.controller;

import com.example.eventscheduler.domain.Event;
import com.example.eventscheduler.service.EventService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;
import java.util.List;

/**
 * REST controller for managing events.
 * Exposes API endpoints for creating, querying, and completing events.
 */
@RestController
@RequestMapping("/api/events")
public class EventController {

    private final EventService eventService;

    /**
     * Constructs an EventController with the EventService dependency.
     * @param eventService The service handling event business logic.
     */
    public EventController(EventService eventService) {
        this.eventService = eventService;
    }

    /**
     * Creates a new event.
     * @param name The name of the event.
     * @param scheduledTime The scheduled time in ISO-8601 format (e.g., "2025-06-23T18:00:00Z").
     * @return ResponseEntity with the created Event.
     */
    @PostMapping
    public ResponseEntity<Event> createEvent(@RequestParam String name, @RequestParam String scheduledTime) {
        Instant time = Instant.parse(scheduledTime);
        Event event = eventService.createEvent(name, time);
        return ResponseEntity.ok(event);
    }

    /**
     * Retrieves a list of events that are currently "due" (scheduled before the current time).
     * @return ResponseEntity with a list of due Events.
     */
    @GetMapping("/due")
    public ResponseEntity<List<Event>> getDueEvents() {
        List<Event> events = eventService.getDueEvents();
        return ResponseEntity.ok(events);
    }

    /**
     * Marks a specific event as completed.
     * @param id The ID of the event to complete.
     * @return ResponseEntity with no content (200 OK).
     */
    @PostMapping("/{id}/complete")
    public ResponseEntity<Void> completeEvent(@PathVariable Long id) {
        eventService.completeEvent(id);
        return ResponseEntity.ok().build();
    }
}
