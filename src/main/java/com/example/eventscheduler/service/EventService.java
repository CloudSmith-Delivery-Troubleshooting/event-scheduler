package com.example.eventscheduler.service;

import com.example.eventscheduler.domain.Event;
import com.example.eventscheduler.domain.EventStatus;
import com.example.eventscheduler.exception.EventNotFoundException;
import com.example.eventscheduler.repository.EventRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.List;

/**
 * Business logic service for managing Events.
 * Handles creation, retrieval, and completion of events.
 */
@Service
public class EventService {

    private final EventRepository eventRepository;
    private final ClockService clockService;
    private final NotificationService notificationService;
    private final SharedCounterService sharedCounterService;

    /**
     * Constructs an EventService with its required dependencies.
     * Spring automatically injects these beans.
     * @param eventRepository The repository for Event persistence.
     * @param clockService The service for time-related operations.
     * @param notificationService The service for sending notifications.
     * @param sharedCounterService The service managing a shared counter.
     */
    public EventService(EventRepository eventRepository,
                        ClockService clockService,
                        NotificationService notificationService,
                        SharedCounterService sharedCounterService) {
        this.eventRepository = eventRepository;
        this.clockService = clockService;
        this.notificationService = notificationService;
        this.sharedCounterService = sharedCounterService;
    }

    /**
     * Creates a new event and persists it to the database.
     * Also increments a shared counter.
     * @param name The name of the event.
     * @param scheduledTime The time at which the event is scheduled.
     * @return The created and saved Event entity.
     */
    @Transactional
    public Event createEvent(String name, Instant scheduledTime) {
        Event event = Event.builder()
                .name(name)
                .scheduledTime(scheduledTime)
                .status(EventStatus.SCHEDULED)
                .build();
        Event saved = eventRepository.save(event);
        sharedCounterService.incrementAndGet();
        return saved;
    }

    /**
     * Retrieves all events that are scheduled and whose scheduled time is before the current time.
     * @return A list of due events.
     */
    @Transactional(readOnly = true)
    public List<Event> getDueEvents() {
        Instant now = clockService.now();
        return eventRepository.findByStatusAndScheduledTimeBefore(EventStatus.SCHEDULED.name(), now);
    }

    /**
     * Marks an event as completed and sends a notification.
     * Throws EventNotFoundException if the event does not exist.
     * @param eventId The ID of the event to complete.
     */
    @Transactional
    public void completeEvent(Long eventId) {
        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new EventNotFoundException(eventId));
        event.setStatus(EventStatus.COMPLETED);
        eventRepository.save(event);
        notificationService.notifyUser("Event completed: " + event.getName());
    }
}
