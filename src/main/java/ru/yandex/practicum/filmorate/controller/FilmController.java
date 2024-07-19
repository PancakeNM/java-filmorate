package ru.yandex.practicum.filmorate.controller;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.yandex.practicum.filmorate.exception.ConditionsNotMetException;
import ru.yandex.practicum.filmorate.exception.DuplicateDataException;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Film;

import java.time.LocalDate;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/films")
@Slf4j
public class FilmController {
    private final Map<Long, Film> films = new HashMap<>();

    @GetMapping
    public Collection<Film> getAll() {
        return films.values();
    }

    @PostMapping
    public Film create(@Valid @RequestBody Film newFilm) {
        log.info("Adding new film");
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
        for (Film film: films.values()) {
            if (film.getName().equals(newFilm.getName())) {
                log.warn("Film with name {} is already exist", newFilm.getName());
                throw new DuplicateDataException("Фильм с данным именем уже существует");
            }
        }
        log.info("Generating new id");
        newFilm.setId(generateNewId());
        films.put(newFilm.getId(), newFilm);
        log.info("New film added");
        return newFilm;
    }

    @PutMapping
    public Film update(@Valid @RequestBody Film newFilm) {
        if (newFilm.getId() == null) {
            log.warn("Film id is not stated");
            throw new ConditionsNotMetException("Не указан id фильма");
        }
        if (films.containsKey(newFilm.getId())) {
            Film oldFilm = films.get(newFilm.getId());
            if (!oldFilm.getName().equals(newFilm.getName())) {
                for (Film film: films.values()) {
                    if (film.getName().equals(newFilm.getName())) {
                        log.warn("Film with name {} is already exist", newFilm.getName());
                        throw new DuplicateDataException("Фильм с именем = " + newFilm.getName() + " уже существует");
                    }
                }
            }
            log.info("Film with id {} is updated", newFilm.getId());
            return filmUpdater(oldFilm, newFilm);
        }
        log.warn("Film with id {} is not found", newFilm.getId());
        throw new NotFoundException("Фильм с id = " + newFilm.getId() + " не найден");
    }

    private Film filmUpdater(Film oldFilm, Film newFilm) {
        log.debug("Film update started");
        if (newFilm.getName() != null) {
            oldFilm.setName(newFilm.getName());
            log.debug("Film name update");
        }
        if (newFilm.getDescription() != null) {
            oldFilm.setDescription(newFilm.getDescription());
            log.debug("Film desc updated");
        }
        if (newFilm.getDuration() != null) {
            oldFilm.setDuration(newFilm.getDuration());
            log.debug("Film duration updated");
        }
        if (newFilm.getReleaseDate() != null) {
            oldFilm.setReleaseDate(newFilm.getReleaseDate());
            log.debug("Film release date updated");
        }
        log.debug("Film updated");
        return oldFilm;
    }

    private Long generateNewId() {
        long currentMaxId = films.keySet()
                .stream()
                .mapToLong(id -> id)
                .max()
                .orElse(0);
        return ++currentMaxId;
    }
}
