package com.example.eventscheduler.controller;

import com.example.eventscheduler.domain.Event;
import com.example.eventscheduler.domain.EventStatus;
import com.example.eventscheduler.service.EventService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.Instant;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Unit tests for EventController using @WebMvcTest and MockMvc.
 * Service layer is mocked to isolate controller behavior.
 */
@WebMvcTest(EventController.class)
class EventControllerTest {

    @Autowired
    private MockMvc mockMvc;

    private EventService eventService;

    @Test
    @DisplayName("POST /api/events - Success")
    void testCreateEventSuccess() throws Exception {
        String eventName = "Test Event";
        String scheduledTime = "2025-06-23T18:00:00Z";

        Event createdEvent = Event.builder()
                .id(1L)
                .name(eventName)
                .scheduledTime(Instant.parse(scheduledTime))
                .status(EventStatus.SCHEDULED)
                .build();

        when(eventService.createEvent(eq(eventName), any(Instant.class))).thenReturn(createdEvent);

        mockMvc.perform(post("/api/events")
                                .param("name", eventName)
                                .param("scheduledTime", scheduledTime)
                                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(createdEvent.getId()))
                .andExpect(jsonPath("$.name").value(eventName))
                .andExpect(jsonPath("$.status").value(EventStatus.SCHEDULED.name()));

        verify(eventService, times(1)).createEvent(eq(eventName), any(Instant.class));
    }

    @Test
    @DisplayName("GET /api/events/due - Success")
    void testGetDueEventsSuccess() throws Exception {
        Event event1 = Event.builder()
                .id(1L)
                .name("Event 1")
                .scheduledTime(Instant.parse("2025-06-23T17:00:00Z"))
                .status(EventStatus.SCHEDULED)
                .build();

        Event event2 = Event.builder()
                .id(2L)
                .name("Event 2")
                .scheduledTime(Instant.parse("2025-06-23T16:00:00Z"))
                .status(EventStatus.SCHEDULED)
                .build();

        when(eventService.getDueEvents()).thenReturn(List.of(event1, event2));

        mockMvc.perform(get("/api/events/due")
                                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].id").value(event1.getId()))
                .andExpect(jsonPath("$[0].name").value(event1.getName()))
                .andExpect(jsonPath("$[1].id").value(event2.getId()))
                .andExpect(jsonPath("$[1].name").value(event2.getName()));

        verify(eventService, times(1)).getDueEvents();
    }

    @Test
    @DisplayName("POST /api/events/{id}/complete - Success")
    void testCompleteEventSuccess() throws Exception {
        Long eventId = 1L;

        doNothing().when(eventService).completeEvent(eventId);

        mockMvc.perform(post("/api/events/{id}/complete", eventId)
                                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        verify(eventService, times(1)).completeEvent(eventId);
    }

    @Test
    @DisplayName("POST /api/events/{id}/complete - Event Not Found")
    void testCompleteEventNotFound() throws Exception {
        Long eventId = 999L;

        doThrow(new com.example.eventscheduler.exception.EventNotFoundException(eventId))
                .when(eventService).completeEvent(eventId);

        mockMvc.perform(post("/api/events/{id}/complete", eventId)
                                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());

        verify(eventService, times(1)).completeEvent(eventId);
    }
}
