package ru.yandex.practicum.filmorate.service.genre;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.storage.genre.GenreStorage;

import java.util.Collection;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class GenreService {

    private final GenreStorage genreStorage;

    public Genre getGenreById(Long id) {
        Optional<Genre> genre = genreStorage.getGenreById(id);
        if (genre.isEmpty()) {
            throw new NotFoundException(String.format("Genre with id %d not found", id));
        } else {
            return genre.get();
        }
    }

    public Collection<Genre> getAllGenres() {
        return genreStorage.getAllGenres();
    }
}