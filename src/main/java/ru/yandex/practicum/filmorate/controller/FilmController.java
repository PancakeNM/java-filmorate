package ru.yandex.practicum.filmorate.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.yandex.practicum.filmorate.exception.ConditionsNotMetException;
import ru.yandex.practicum.filmorate.exception.DuplicateDataException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.film.FilmService;

import java.time.LocalDate;
import java.util.Collection;

@RequiredArgsConstructor
@RestController
@RequestMapping("/films")
@Slf4j
public class FilmController {

    private final FilmService filmService;


    @GetMapping
    public Collection<Film> getAll() {
        return filmService.getAll();
    }

    @PostMapping
    public Film create(@RequestBody Film newFilm) {
        log.info("Adding new film");
        validate(newFilm);
        return filmService.save(newFilm);
    }

    @PutMapping
    public Film update(@RequestBody Film newFilm) {
        if (newFilm.getId() == null) {
            log.warn("Film id is not stated");
            throw new ConditionsNotMetException("Не указан id фильма");
        }
        return filmService.update(newFilm);
    }

    private void validate(Film newFilm) {
        if (newFilm.getName() == null || newFilm.getName().isBlank()) {
            log.warn("Film name is null or blank");
            throw new ConditionsNotMetException("Имя не может быть пустым");
        } else if (newFilm.getDescription().length() > 200) {
            log.warn("Film description length is exceeding 200 chars limit");
            throw new ConditionsNotMetException("Описание не может быть длиннее 200 символов");
        } else if (newFilm.getReleaseDate().isBefore(LocalDate.of(1896, 12, 28))) {
            log.warn("Film release date is before 1869/12/28");
            throw new ConditionsNotMetException("Дата релиза не может быть раньше 28 декабря 1869 года");
        } else if (newFilm.getDuration() < 0) {
            log.warn("Film duration value is negative");
            throw new ConditionsNotMetException("Продолжительность фильма должна быть положительным числом");
        }
        for (Film film: filmService.getAll()) {
            if (film.getName().equals(newFilm.getName())) {
                log.warn("Film with name {} is already exist", newFilm.getName());
                throw new DuplicateDataException("Фильм с именем " + newFilm.getName() + " уже существует");
            }
        }
    }

    private void validate(Long filmId, Long userId) {
        if (filmId == null) {
            log.warn("filmId is null");
            throw new ConditionsNotMetException("Не указан id фильма.");
        } else if (userId == null) {
            log.warn("userId is null");
            throw new ConditionsNotMetException("Не указан id пользователя.");
        }
    }
}
