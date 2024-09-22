package ru.yandex.practicum.filmorate.service.film;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;

import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
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

    public void removeLike(Long filmId, Long userId) {
        getFilmById(filmId).removeLike(userId);
        getFilmById(filmId);
    }

    public Film getFilmById(Long filmId) {
        return filmStorage.getById(filmId);
    }

}
