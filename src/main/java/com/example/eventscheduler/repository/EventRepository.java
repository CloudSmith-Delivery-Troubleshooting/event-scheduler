package com.example.eventscheduler.repository;

import com.example.eventscheduler.domain.Event;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.Instant;
import java.util.List;

/**
 * Spring Data JPA repository for {@link Event} entities.
 * Provides standard CRUD operations and custom query methods.
 */
public interface EventRepository extends JpaRepository<Event, Long> {

    /**
     * Finds events by their status and scheduled time before a given instant.
     * This method demonstrates Spring Data JPA's query derivation.
     * @param status The status of the events to find (e.g., "SCHEDULED").
     * @param time The instant before which events should be scheduled.
     * @return A list of matching events.
     */
    List<Event> findByStatusAndScheduledTimeBefore(String status, Instant time);
}
