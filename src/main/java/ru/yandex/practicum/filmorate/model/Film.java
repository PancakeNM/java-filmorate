package ru.yandex.practicum.filmorate.model;

import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDate;
import java.util.Collection;

/**
 * Film.
 */
@Data
@EqualsAndHashCode(of = "name")
@Builder(toBuilder = true)
public class Film {
    private Long id;
    private String name;
    private String description;
    private LocalDate releaseDate;
    private Integer duration;
    private Collection<Like> likes;
    private Collection<Genre> genres;
    private Mpa mpa;
    private int rate;
}
