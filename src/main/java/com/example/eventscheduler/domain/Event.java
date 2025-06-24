package com.example.eventscheduler.domain;

import jakarta.persistence.*;
import lombok.*;

import java.time.Instant;

/**
 * Represents an Event entity in the scheduling system.
 * Mapped to the "events" table in the database.
 */
@Entity
@Table(name = "events")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Event {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private Instant scheduledTime;

    @Enumerated(EnumType.STRING)
    private EventStatus status;
}
