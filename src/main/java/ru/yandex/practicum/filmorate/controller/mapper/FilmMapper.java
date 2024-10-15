package ru.yandex.practicum.filmorate.controller.mapper;

import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.dto.film.FilmDto;
import ru.yandex.practicum.filmorate.model.Film;

@Service
public class FilmMapper {

    public Film map(FilmDto dto) {
        return Film.builder()
                .id(dto.getId())
                .name(dto.getName())
                .description(dto.getDescription())
                .releaseDate(dto.getReleaseDate())
                .duration(dto.getDuration())
                .genres(dto.getGenres())
                .mpa(dto.getMpa())
                .rate(dto.getRate())
                .build();
    }

    public FilmDto map(Film film) {
        return FilmDto.builder()
                .id(film.getId())
                .description(film.getDescription())
                .duration(film.getDuration())
                .genres(film.getGenres())
                .mpa(film.getMpa())
                .name(film.getName())
                .releaseDate(film.getReleaseDate())
                .rate(film.getRate())
                .build();
    }
}
