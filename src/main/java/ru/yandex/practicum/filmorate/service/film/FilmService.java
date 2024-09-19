package ru.yandex.practicum.filmorate.service.film;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;

import java.util.Collection;

@Service
@RequiredArgsConstructor
public class FilmService {

    private final FilmStorage filmStorage;

    public Film save(Film film) {
        return filmStorage.save(film);
    }

    public Film update(Film film) {
        return filmStorage.update(film);
    }

    public Collection<Film> getAll () {
        return filmStorage.getAll();
    }

    public Film addLike(Long filmId, Long userId) {
        getFilmById(filmId).addLike(userId);
        return getFilmById(filmId);
    }

    public Film removeLike(Long filmId, Long userId) {
        getFilmById(filmId).removeLike(userId);
        return getFilmById(filmId);
    }

    public Film getFilmById(Long filmId) {
        return filmStorage.getById(filmId);
    }

}
