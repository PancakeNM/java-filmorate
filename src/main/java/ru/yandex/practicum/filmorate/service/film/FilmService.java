package ru.yandex.practicum.filmorate.service.film;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;
import ru.yandex.practicum.filmorate.storage.like.LikeStorage;

import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Slf4j
public class FilmService {

    private final FilmStorage filmStorage;
    private final LikeStorage likeStorage;

    public FilmService(@Qualifier("filmDbStorage") FilmStorage filmStorage,
                       @Qualifier("likeDbStorage") LikeStorage likeStorage) {
        this.filmStorage = filmStorage;
        this.likeStorage = likeStorage;
    }

    public Film save(Film film) {
        return filmStorage.save(film);
    }

    public Film update(Film film) {
        return filmStorage.updateFilm(film);
    }

    public Collection<Film> getAll() {
        return filmStorage.getAll();
    }

    public void addLike(Long filmId, Long userId) {
        likeStorage.addLike(filmId, userId);
    }

    public void removeLike(Long filmId, Long userId) {
        likeStorage.removeLike(filmId, userId);
    }

    public Film getFilmById(Long filmId) {
        Optional<Film> optionalFilm = filmStorage.getById(filmId);
        if (optionalFilm.isEmpty()) {
            throw new NotFoundException(String.format("Фильм с id %s не найден", filmId));
        } else {
            return optionalFilm.get();
        }
    }

    public List<Film> getMostPopular(Integer count) {
        return filmStorage.getAll().stream()
                .filter(film -> film.getRate() > 0)
                .filter(film -> film.getLikes().size() > 0)
                .sorted((f1, f2) -> f2.getRate() - f1.getRate())
                .limit(count)
                .collect(Collectors.toList());
    }

}
