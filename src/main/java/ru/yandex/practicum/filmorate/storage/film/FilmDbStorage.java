package ru.yandex.practicum.filmorate.storage.film;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Film;

import java.util.Collection;
import java.util.List;

@Component
@Slf4j
public class FilmDbStorage implements FilmStorage {
    @Override
    public Film save(Film film) {
        return null;
    }

    @Override
    public Film update(Film film) {
        return null;
    }

    @Override
    public Film getById(Long filmId) {
        return null;
    }

    @Override
    public Collection<Film> getAll() {
        return null;
    }

    @Override
    public Film addLike(Long filmId, Long userId) {
        return null;
    }

    @Override
    public void removeLike(Long filmId, Long userId) {

    }

    @Override
    public List<Film> getMostPopular(Integer count) {
        return null;
    }
}
