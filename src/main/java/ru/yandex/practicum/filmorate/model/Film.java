package ru.yandex.practicum.filmorate.model;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.Duration;
import java.time.Instant;

/**
 * Film.
 */
@Data
@EqualsAndHashCode(of = "id")
public class Film {
    private Long id;
    private String name;
    private String description;
    private Instant releaseDate;
    private Duration duration;
}
