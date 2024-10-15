package ru.yandex.practicum.filmorate.storage.film;

import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.dal.BaseRepository;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.storage.filmGenre.FilmGenreStorage;
import ru.yandex.practicum.filmorate.storage.mpa.MpaStorage;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Component
@Slf4j
@Repository
public class FilmDbStorage extends BaseRepository<Film> implements FilmStorage {

    private static final String FIND_ALL_QUERY = "SELECT * FROM films";
    private static final String FIND_MOST_POPULAR_QUERY = "SELECT * FROM films WHERE id IN " +
                                                      "(SELECT film_id FROM likes GROUP BY film_id " +
                                                      "ORDER BY COUNT(user_id)) LIMIT ?";
    private static final String INSERT_QUERY = "INSERT INTO films VALUES (?, ?, ?, ?, ?) returning id";
    private static final String UPDATE_QUERY = "UPDATE films SET name = ?, description = ?, release_date = ?, " +
            "duration = , mpa_rating = ? WHERE id = ?";

    private final FilmGenreStorage filmGenreStorage;
    private final MpaStorage mpaStorage;

    public FilmDbStorage(JdbcTemplate jdbc,
                         RowMapper<Film> mapper,
                         FilmGenreStorage filmGenreStorage,
                         MpaStorage mpaStorage) {
        super(jdbc, mapper);
        this.filmGenreStorage = filmGenreStorage;
        this.mpaStorage = mpaStorage;
    }

    @Override
    public Film save(Film film) {
        long id = insert(
                INSERT_QUERY,
                film.getName(),
                film.getDescription(),
                film.getReleaseDate(),
                film.getDuration(),
                film.getMpa().getId()
        );
        film.setId(id);
        return film;
    }

    @Override
    public Film updateFilm(Film film) {
        update(
                UPDATE_QUERY,
                film.getName(),
                film.getDescription(),
                film.getReleaseDate(),
                film.getDuration(),
                film.getMpa().getId()
        );
        return addFields(film);
    }

    @Override
    public Optional<Film> getById(Long filmId) {
        return findOne(FIND_ALL_QUERY.concat(" WHERE id = ?"), filmId);
    }

    @Override
    public Collection<Film> getAll() {
        Collection<Film> films = findMany(FIND_ALL_QUERY);
        return setFilmGenres(films);
    }

    @Deprecated
    @Override
    public Film addLike(Long filmId, Long userId) {
        return null;
    }

    @Deprecated
    @Override
    public void removeLike(Long filmId, Long userId) {

    }

    @Override
    public List<Film> getMostPopular(Integer count) {
        return findMany(FIND_MOST_POPULAR_QUERY, count);
    }

    private Film addFields(Film film) {
        long filmId = film.getId();
        Long mpaId = film.getMpa().getId();
        if (film.getGenres() != null) {
            film.getGenres().forEach(genre -> filmGenreStorage.addFilmGenre(filmId, genre.getId()));
        }
        Collection<Genre> filmGenres = filmGenreStorage.getAllFilmGenresByFilmId(film.getId());
        Mpa filmMpa = mpaStorage.getMpaById(mpaId).get();
        return film.toBuilder().mpa(filmMpa).genres(filmGenres).build();
    }

    private Collection<Film> setFilmGenres(Collection<Film> films) {
        Map<Long, Collection<Genre>> filmGenresMap = filmGenreStorage.getAllFilmGenres(films);
        films.forEach(film -> {
            Long filmId = film.getId();
            film.setGenres(filmGenresMap.getOrDefault(filmId, new ArrayList<>()));
        });

        return films;
    }
}
