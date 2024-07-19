package ru.yandex.practicum.filmorate.controller;

import jakarta.validation.Valid;
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
public class FilmController {
    private final Map<Long, Film> films = new HashMap<>();

    @GetMapping
    public Collection<Film> getAll() {
        return films.values();
    }

    @PostMapping
    public Film create(@Valid @RequestBody Film newFilm) {
        if (newFilm.getName() == null || newFilm.getName().isBlank()) {
            throw new ConditionsNotMetException("Имя не может быть пустым");
        } else if (newFilm.getDescription().length() > 200) {
            throw new ConditionsNotMetException("Описание не может быть длиннее 200 символов");
        } else if (newFilm.getReleaseDate().isBefore(LocalDate.of(1896, 12, 28))) {
            throw new ConditionsNotMetException("Дата релиза не может быть раньше 28 декабря 1869 года");
        } else if (newFilm.getDuration() < 0) {
            throw new ConditionsNotMetException("Продолжительность фильма должна быть положительным числом");
        }
        for (Film film: films.values()) {
            if (film.getName().equals(newFilm.getName())) {
                throw new DuplicateDataException("Фильм с данным именем уже существует");
            }
        }
        newFilm.setId(generateNewId());
        films.put(newFilm.getId(), newFilm);
        return newFilm;
    }

    @PutMapping
    public Film update(@Valid @RequestBody Film newFilm) {
        if (newFilm.getId() == null) {
            throw new ConditionsNotMetException("Не указан id фильма");
        }
        if (films.containsKey(newFilm.getId())) {
            Film oldFilm = films.get(newFilm.getId());
            for (Film film: films.values()) {
                if (film.getName().equals(newFilm.getName())) {
                    throw new DuplicateDataException("Фильм с именем = " + newFilm.getName() + " уже существует");
                }
            }
            return filmUpdater(oldFilm, newFilm);
        }
        throw new NotFoundException("Фильм с id = " + newFilm.getId() + " не найден");
    }

    private Film filmUpdater(Film oldFilm, Film newFilm) {
        if (newFilm.getName() != null) {
            oldFilm.setName(newFilm.getName());
        }
        if (newFilm.getDescription() != null) {
            oldFilm.setDescription(newFilm.getDescription());
        }
        if (newFilm.getDuration() != null) {
            oldFilm.setDuration(newFilm.getDuration());
        }
        if (newFilm.getReleaseDate() != null) {
            oldFilm.setReleaseDate(newFilm.getReleaseDate());
        }
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
