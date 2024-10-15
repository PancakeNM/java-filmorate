package ru.yandex.practicum.filmorate.storage.film;

import ru.yandex.practicum.filmorate.model.Film;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

public interface FilmStorage {

    Film save(Film film);

    Film updateFilm(Film film);

    Optional<Film> getById(Long filmId);

    Collection<Film> getAll();

    Film addLike(Long filmId, Long userId);

    void removeLike(Long filmId, Long userId);

    List<Film> getMostPopular(Integer count);
}
