package ru.yandex.practicum.filmorate.storage.film;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.DuplicateDataException;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Film;

import java.util.*;
import java.util.stream.Collectors;

@Component
@Slf4j
@Primary
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
                throw new DuplicateDataException(String.format("Фильм с именем %s уже существует", newFilm.getName()));
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
                        throw new DuplicateDataException(String.format("Фильм с именем = %s уже существует",
                                newFilm.getName()));
                    }
                }
            }
            log.info("Film with id {} is updated", newFilm.getId());
            return filmUpdater(oldFilm, newFilm);
        }
        log.warn("Film with id {} is not found", newFilm.getId());
        throw new NotFoundException(String.format("Фильм с id = %s не найден", newFilm.getId()));
    }

    @Override
    public Collection<Film> getAll() {
        return films.values();
    }

    @Override
    public Film getById(Long filmId) {
        if (!films.containsKey(filmId)) {
            log.warn("Film with id {} is not found", filmId);
            throw new NotFoundException(String.format("Фильм с id = %s не найден", filmId));
        }
        return films.get(filmId);
    }
    
    @Override
    public Film addLike(Long filmId, Long userId) {
        getById(filmId).addLike(userId);
        return getById(filmId);
    }
    
    @Override
    public void removeLike(Long filmId, Long userId) {
        getById(filmId).removeLike(userId);
    }
    
    @Override
    public List<Film> getMostPopular(Integer count) {
        log.info("generating list of popular films.");
        return getAll().stream()
                .sorted(Comparator.comparing(Film::getLikesCount).reversed())
                .limit(count)
                .collect(Collectors.toList());
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
