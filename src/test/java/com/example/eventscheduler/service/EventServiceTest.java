package com.example.eventscheduler.service;

import com.example.eventscheduler.domain.Event;
import com.example.eventscheduler.domain.EventStatus;
import com.example.eventscheduler.exception.EventNotFoundException;
import com.example.eventscheduler.repository.EventRepository;
import com.example.eventscheduler.util.SystemClock;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

import java.time.Clock;
import java.time.Instant;
import java.time.ZoneId;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Unit tests for {@link EventService}.
 * Mocks dependencies to test EventService's business logic in isolation.
 */
class EventServiceTest {

    // Dependencies to be mocked
    private EventRepository eventRepository;
    private ClockService clockService;
    private NotificationService notificationService;
    private SharedCounterService sharedCounterService;

    // The service under test
    private EventService eventService;

    @BeforeEach
    void setup() {
        // Initialize mocks for each test run
        eventRepository = mock(EventRepository.class);
        notificationService = mock(NotificationService.class);

        // For ClockService and SystemClock, create real instances but with a fixed Clock
        // This simulates a "frozen clock" for predictable time-dependent tests
        Clock fixedClock = Clock.fixed(Instant.parse("2025-06-23T17:00:00Z"), ZoneId.of("UTC"));
        SystemClock systemClock = new SystemClock(fixedClock);
        clockService = new ClockService(systemClock);

        // For SharedCounterService, use a real instance to test its state changes,
        // and reset it before each test to ensure test isolation.
        sharedCounterService = new SharedCounterService();
        sharedCounterService.reset(); // Important: Reset for each test to avoid flaky tests

        // Initialize the service with its mocked and real (controlled) dependencies
        eventService = new EventService(eventRepository, clockService, notificationService, sharedCounterService);
    }

    @Test
    void createEvent_shouldSaveEventAndIncrementCounter() {
        // Given
        String eventName = "My New Event";
        Instant scheduledTime = Instant.parse("2025-07-01T10:00:00Z");

        // Mock the save operation to return a saved event with an ID
        Event unsavedEvent = Event.builder().name(eventName).scheduledTime(scheduledTime).status(EventStatus.SCHEDULED).build();
        Event savedEvent = Event.builder().id(1L).name(eventName).scheduledTime(scheduledTime).status(EventStatus.SCHEDULED).build();
        when(eventRepository.save(any(Event.class))).thenReturn(savedEvent);

        // When
        Event result = eventService.createEvent(eventName, scheduledTime);

        // Then
        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals(eventName, result.getName());
        assertEquals(scheduledTime, result.getScheduledTime());
        assertEquals(EventStatus.SCHEDULED, result.getStatus());

        // Verify that eventRepository.save was called exactly once with an Event containing the correct details
        ArgumentCaptor<Event> eventCaptor = ArgumentCaptor.forClass(Event.class);
        verify(eventRepository, times(1)).save(eventCaptor.capture());
        assertEquals(eventName, eventCaptor.getValue().getName());
        assertEquals(scheduledTime, eventCaptor.getValue().getScheduledTime());
        assertEquals(EventStatus.SCHEDULED, eventCaptor.getValue().getStatus());

        // Verify that the shared counter was incremented
        assertEquals(1, sharedCounterService.get());
    }

    @Test
    void getDueEvents_shouldReturnEventsScheduledBeforeCurrentTime() {
        // Given
        // The clock is fixed at "2025-06-23T17:00:00Z" by the setup method
        Instant fixedNow = Instant.parse("2025-06-23T17:00:00Z");

        Event event1 = Event.builder().id(1L).name("Past Event").scheduledTime(fixedNow.minusSeconds(3600)).status(EventStatus.SCHEDULED).build();
        Event event2 = Event.builder().id(2L).name("Future Event").scheduledTime(fixedNow.plusSeconds(3600)).status(EventStatus.SCHEDULED).build();
        Event event3 = Event.builder().id(3L).name("Due Event").scheduledTime(fixedNow.minusSeconds(10)).status(EventStatus.SCHEDULED).build();

        // Mock repository behavior: only event1 and event3 should be returned based on scheduled time
        when(eventRepository.findByStatusAndScheduledTimeBefore(EventStatus.SCHEDULED.name(), fixedNow))
                .thenReturn(List.of(event1, event3));

        // When
        List<Event> dueEvents = eventService.getDueEvents();

        // Then
        assertNotNull(dueEvents);
        assertEquals(2, dueEvents.size());
        assertTrue(dueEvents.contains(event1));
        assertTrue(dueEvents.contains(event3));
        assertFalse(dueEvents.contains(event2)); // Ensure future event is not included

        // Verify that the repository method was called with the correct status and current (fixed) time
        verify(eventRepository, times(1)).findByStatusAndScheduledTimeBefore(EventStatus.SCHEDULED.name(), fixedNow);
    }

    @Test
    void completeEvent_shouldUpdateStatusAndNotifyUser() {
        // Given
        Long eventId = 1L;
        Event eventToComplete = Event.builder().id(eventId).name("Event to Complete").scheduledTime(Instant.now()).status(EventStatus.SCHEDULED).build();

        when(eventRepository.findById(eventId)).thenReturn(Optional.of(eventToComplete));
        when(eventRepository.save(any(Event.class))).thenReturn(eventToComplete); // Return the same event after saving

        // When
        eventService.completeEvent(eventId);

        // Then
        // Verify event status is updated to COMPLETED
        assertEquals(EventStatus.COMPLETED, eventToComplete.getStatus());

        // Verify that eventRepository.findById was called
        verify(eventRepository, times(1)).findById(eventId);

        // Verify that eventRepository.save was called with the updated event
        ArgumentCaptor<Event> eventCaptor = ArgumentCaptor.forClass(Event.class);
        verify(eventRepository, times(1)).save(eventCaptor.capture());
        assertEquals(EventStatus.COMPLETED, eventCaptor.getValue().getStatus());
        assertEquals(eventId, eventCaptor.getValue().getId());

        // Verify that notificationService.notifyUser was called with the correct message
        verify(notificationService, times(1)).notifyUser("Event completed: Event to Complete");
    }

    @Test
    void completeEvent_shouldThrowEventNotFoundException_whenEventDoesNotExist() {
        // Given
        Long nonExistentEventId = 99L;
        when(eventRepository.findById(nonExistentEventId)).thenReturn(Optional.empty());

        // When/Then
        // Expect an EventNotFoundException to be thrown
        EventNotFoundException thrown = assertThrows(EventNotFoundException.class, () -> {
            eventService.completeEvent(nonExistentEventId);
        });

        // Verify the exception message
        assertEquals("Event not found with id: " + nonExistentEventId, thrown.getMessage());

        // Verify that no further interactions (like save or notify) occurred
        verify(eventRepository, never()).save(any(Event.class));
        verify(notificationService, never()).notifyUser(anyString());
    }
}
