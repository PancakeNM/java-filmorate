package ru.yandex.practicum.filmorate.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.yandex.practicum.filmorate.controller.mapper.FilmMapper;
import ru.yandex.practicum.filmorate.dto.film.CreateFilmDto;
import ru.yandex.practicum.filmorate.dto.film.FilmDto;
import ru.yandex.practicum.filmorate.exception.ConditionsNotMetException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.film.FilmService;

import java.time.LocalDate;
import java.util.Collection;
import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/films")
@Slf4j
public class FilmController {

    private final FilmService filmService;
    private final String likePath = "/{film-id}/like/{user-id}";
    private final FilmMapper mapper;


    @GetMapping
    public Collection<Film> getAll() {
        return filmService.getAll();
    }

    @PostMapping
    public FilmDto create(@RequestBody CreateFilmDto filmDto) {
        log.info("Adding new film");
        Film newFilm = mapper.map(filmDto);
        validate(newFilm);
        Film createdFilm = filmService.save(newFilm);
        return mapper.map(createdFilm);
    }

    @PutMapping
    public FilmDto update(@RequestBody FilmDto filmDto) {
        Film newFilm = mapper.map(filmDto);
        if (newFilm.getId() == null) {
            log.warn("Film id is not stated");
            throw new ConditionsNotMetException("Не указан id фильма");
        }
        Film updatedFilm = filmService.update(newFilm);
        return mapper.map(updatedFilm);
    }

    @PutMapping(likePath)
    public FilmDto addLike(@PathVariable(name = "film-id") Long filmId, @PathVariable("user-id") Long id) {
        validate(filmId, id);
        filmService.addLike(filmId, id);
        return mapper.map(filmService.getFilmById(filmId));
    }

    @DeleteMapping(likePath)
    public FilmDto removeLike(@PathVariable(name = "film-id") Long filmId, @PathVariable(name = "user-id")
                                Long userId) {
        validate(filmId, userId);
        filmService.removeLike(filmId, userId);
        return mapper.map(filmService.getFilmById(filmId));
    }

    @GetMapping("/popular")
    public List<FilmDto> getMostPopular(@RequestParam(name = "count", defaultValue = "10") int count) {
        return filmService.getMostPopular(count).stream()
                .map(mapper::map)
                .toList();
    }

    private void validate(Film newFilm) {
        if (newFilm.getName() == null || newFilm.getName().isBlank()) {
            log.warn("Film name is null or blank");
            throw new ConditionsNotMetException("Имя не может быть пустым");
        } else if (newFilm.getDescription().length() > 200) {
            log.warn("Film description length is exceeding 200 chars limit");
            throw new ConditionsNotMetException("Описание не может быть длиннее 200 символов");
        } else if (newFilm.getReleaseDate().isBefore(LocalDate.of(1896, 12, 28))) {
            log.warn("Film release date is before 1869/12/28");
            throw new ConditionsNotMetException("Дата релиза не может быть раньше 28 декабря 1869 года");
        } else if (newFilm.getDuration() < 0) {
            log.warn("Film duration value is negative");
            throw new ConditionsNotMetException("Продолжительность фильма должна быть положительным числом");
        }
    }

    private void validate(Long filmId, Long userId) {
        if (filmId == null) {
            log.warn("filmId is null");
            throw new ConditionsNotMetException("Не указан id фильма.");
        } else if (userId == null) {
            log.warn("userId is null");
            throw new ConditionsNotMetException("Не указан id пользователя.");
        }
    }
}
