package ru.yandex.practicum.filmorate.dto.film;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.Mpa;

import java.time.LocalDate;
import java.util.Collection;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreateFilmDto {
    private String name;
    private String description;
    private LocalDate releaseDate;
    private Integer duration;
    private Collection<Genre> genres;
    private Mpa mpa;
    private int rate;
}
