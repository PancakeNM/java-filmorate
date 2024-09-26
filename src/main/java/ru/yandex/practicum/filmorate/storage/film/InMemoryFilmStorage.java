package ru.yandex.practicum.filmorate.storage.film;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.DuplicateDataException;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Film;

import java.util.*;

@Component
@Slf4j
public class InMemoryFilmStorage implements FilmStorage {

    private final Map<Long, Film> films = new HashMap<>();

    private Long generateNewId() {
        long currentMaxId = films.keySet()
                .stream()
                .mapToLong(id -> id)
                .max()
                .orElse(0);
        return ++currentMaxId;
    }

    @Override
    public Film save(Film newFilm) {
        for (Film film: films.values()) {
            if (film.getName().equals(newFilm.getName())) {
                log.warn("Film with name {} is already exist", newFilm.getName());
                throw new DuplicateDataException("Фильм с именем " + newFilm.getName() + " уже существует");
            }
        }
        log.info("Generating new id");
        newFilm.setId(generateNewId());
        films.put(newFilm.getId(), newFilm);
        log.info("New film added");
        return newFilm;
    }

    @Override
    public Film update(Film newFilm) {
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

    @Override
    public Collection<Film> getAll() {
        return films.values();
    }

    @Override
    public Film getById(Long filmId) {
        if (!films.containsKey(filmId)) {
            log.warn("Film with id {} is not found", filmId);
            throw new NotFoundException("Фильм с id = " + filmId + " не найден");
        }
        return films.get(filmId);
    }

    private Film filmUpdater(Film oldFilm, Film newFilm) {
        log.debug("Film update started");
        if (newFilm.getName() != null) {
            oldFilm.setName(newFilm.getName());
            log.debug("Film name updated");
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
}
