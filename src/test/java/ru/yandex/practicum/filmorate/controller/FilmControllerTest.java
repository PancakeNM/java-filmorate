package ru.yandex.practicum.filmorate.controller;


import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.exception.ConditionsNotMetException;
import ru.yandex.practicum.filmorate.exception.DuplicateDataException;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Film;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class FilmControllerTest {
    FilmController controller;
    Film film1;
    Film film2;

    @BeforeEach
    public void beforeEach() {
        controller = new FilmController();
        film1 = new Film();
        film1.setName("TestName1");
        film1.setDescription("TestDescription1");
        film1.setDuration(200);
        film1.setReleaseDate(LocalDate.of(2000, 1, 1));
        film2 = new Film();
        film2.setName("TestName2");
        film2.setDescription("TestDescription2");
        film2.setDuration(200);
        film2.setReleaseDate(LocalDate.of(2001, 1, 1));
    }

    @Test
    public void getAllShouldReturnAllFilms() {
        List<Film> expected = new ArrayList<>();
        expected.add(film1);
        expected.add(film2);
        controller.create(film1);
        controller.create(film2);

        assertEquals(expected, controller.getAll().stream().toList());
    }

    @Test
    public void createShouldThrowConditionsNotMetExceptionIfNameIsNull() {
        film1.setName(null);
        Class<?> expectedClass = ConditionsNotMetException.class;
        Class<?> actualClass = null;
        try {
            controller.create(film1);
        } catch (RuntimeException e) {
            actualClass = e.getClass();
        }
        assertEquals(expectedClass, actualClass);
    }

    @Test
    public void createShouldThrowConditionsNotMetExceptionIfNameIsBlank() {
        film1.setName("  ");
        Class<?> expectedClass = ConditionsNotMetException.class;
        Class<?> actualClass = null;
        try {
            controller.create(film1);
        } catch (RuntimeException e) {
            actualClass = e.getClass();
        }
        assertEquals(expectedClass, actualClass);
    }

    @Test
    public void createShouldThrowConditionsNotMetExceptionIfDescriptionIsGreaterThan_200_Chars() {
        String longString = " ".repeat(201);
        film1.setDescription(longString);
        Class<?> expectedClass = ConditionsNotMetException.class;
        Class<?> actualClass = null;
        try {
            controller.create(film1);
        } catch (RuntimeException e) {
            actualClass = e.getClass();
        }
        assertEquals(expectedClass, actualClass);
    }

    @Test
    public void createShouldThrowConditionsNotMetExceptionIfReleaseDateIsBefore_1896_12_28() {
        film1.setReleaseDate(LocalDate.of(1895, 1, 1));
        Class<?> expectedClass = ConditionsNotMetException.class;
        Class<?> actualClass = null;
        try {
            controller.create(film1);
        } catch (RuntimeException e) {
            actualClass = e.getClass();
        }
        assertEquals(expectedClass, actualClass);
    }

    @Test
    public void createShouldThrowConditionsNotMetExceptionIfDurationIsNegative() {
        film1.setDuration(-200);
        Class<?> expectedClass = ConditionsNotMetException.class;
        Class<?> actualClass = null;
        try {
            controller.create(film1);
        } catch (RuntimeException e) {
            actualClass = e.getClass();
        }
        assertEquals(expectedClass, actualClass);
    }

    @Test
    public void createShouldThrowDuplicateDataExceptionIfNewFilmNameIsAlreadyExists() {
        film2.setName(film1.getName());
        Class<?> expectedClass = DuplicateDataException.class;
        Class<?> actualClass = null;
        controller.create(film1);
        try {
            controller.create(film2);
        } catch (RuntimeException e) {
            actualClass = e.getClass();
        }
        assertEquals(expectedClass, actualClass);
    }

    @Test
    public void updateShouldThrowNotFoundExceptionIfThereIsNoSuchId() {
        controller.create(film1);
        controller.create(film2);
        Film newFilm = new Film();
        newFilm.setId(3L);
        Class<?> expectedClass = NotFoundException.class;
        Class<?> actualClass = null;
        try {
            controller.update(newFilm);
        } catch (RuntimeException e) {
            actualClass = e.getClass();
        }
        assertEquals(expectedClass, actualClass);
    }

    @Test
    public void updateShouldThrowDuplicateDataExceptionIfNewFilmNameIsAlreadyExists() {
        controller.create(film1);
        controller.create(film2);
        Film newFilm = new Film();
        newFilm.setName(film1.getName());
        newFilm.setId(film2.getId());
        Class<?> expectedClass = DuplicateDataException.class;
        Class<?> actualClass = null;
        try {
            controller.update(newFilm);
        } catch (RuntimeException e) {
            actualClass = e.getClass();
        }
        assertEquals(expectedClass, actualClass);
    }

    @Test
    public void updateShouldThrowConditionsNotMetExceptionIfNewFilmIdIsNull() {
        controller.create(film1);
        controller.create(film2);
        Film newFilm = new Film();
        newFilm.setName("UpdateTestFilmName");
        Class<?> expectedClass = ConditionsNotMetException.class;
        Class<?> actualClass = null;
        try {
            controller.update(newFilm);
        } catch (RuntimeException e) {
            actualClass = e.getClass();
        }
        assertEquals(expectedClass, actualClass);
    }

    @Test
    public void createShouldReturnCreatedTask() {
        Film expected = new Film();
        expected.setId(1L);
        expected.setName(film1.getName());
        expected.setDescription(film1.getDescription());
        expected.setReleaseDate(film1.getReleaseDate());
        expected.setDuration(film1.getDuration());

        Film actual = controller.create(film1);

        filmFieldsCheck(expected, actual);
    }

    @Test
    public void updateShouldReturnUpdatedTask() {
        controller.create(film1);
        Film newFilm = new Film();
        newFilm.setId(1L);
        newFilm.setName("UpdateTestName");

        Film expected = new Film();
        expected.setId(1L);
        expected.setName(newFilm.getName());
        expected.setDescription(film1.getDescription());
        expected.setReleaseDate(film1.getReleaseDate());
        expected.setDuration(film1.getDuration());

        Film actual = controller.update(newFilm);

        filmFieldsCheck(expected, actual);
    }

    @Test
    public void updateShouldNotUpdateNameWhenNameIsNull() {
        controller.create(film1);
        Film newFilm = new Film();
        newFilm.setId(1L);
        newFilm.setDescription("UpdateTestDescription");
        newFilm.setReleaseDate(LocalDate.of(2002, 1, 1));
        newFilm.setDuration(180);

        Film expected = new Film();
        expected.setId(1L);
        expected.setName(film1.getName());
        expected.setDescription(newFilm.getDescription());
        expected.setReleaseDate(newFilm.getReleaseDate());
        expected.setDuration(newFilm.getDuration());

        Film actual = controller.update(newFilm);

        filmFieldsCheck(expected, actual);
    }

    @Test
    public void updateShouldNotUpdateDescriptionWhenDescriptionIsNull() {
        controller.create(film1);
        Film newFilm = new Film();
        newFilm.setId(1L);
        newFilm.setName("UpdateTestName");
        newFilm.setReleaseDate(LocalDate.of(2002, 1, 1));
        newFilm.setDuration(180);

        Film expected = new Film();
        expected.setId(1L);
        expected.setName(newFilm.getName());
        expected.setDescription(film1.getDescription());
        expected.setReleaseDate(newFilm.getReleaseDate());
        expected.setDuration(newFilm.getDuration());

        Film actual = controller.update(newFilm);

        filmFieldsCheck(expected, actual);
    }

    @Test
    public void updateShouldNotUpdateReleaseDateWhenReleaseDateIsNull() {
        controller.create(film1);
        Film newFilm = new Film();
        newFilm.setId(1L);
        newFilm.setName("UpdateTestName");
        newFilm.setDescription("UpdateTestDescription");
        newFilm.setDuration(180);

        Film expected = new Film();
        expected.setId(1L);
        expected.setName(newFilm.getName());
        expected.setDescription(newFilm.getDescription());
        expected.setReleaseDate(film1.getReleaseDate());
        expected.setDuration(newFilm.getDuration());

        Film actual = controller.update(newFilm);

        filmFieldsCheck(expected, actual);
    }

    @Test
    public void updateShouldNotDurationWhenDurationIsNull() {
        controller.create(film1);
        Film newFilm = new Film();
        newFilm.setId(1L);
        newFilm.setName("UpdateTestName");
        newFilm.setDescription("UpdateTestDescription");
        newFilm.setReleaseDate(LocalDate.of(2002, 1, 1));

        Film expected = new Film();
        expected.setId(1L);
        expected.setName(newFilm.getName());
        expected.setDescription(newFilm.getDescription());
        expected.setReleaseDate(newFilm.getReleaseDate());
        expected.setDuration(film1.getDuration());

        Film actual = controller.update(newFilm);

        filmFieldsCheck(expected, actual);
    }

    private void filmFieldsCheck(Film expected, Film actual) {
        assertEquals(expected.getId(), actual.getId());
        assertEquals(expected.getName(), actual.getName());
        assertEquals(expected.getDescription(), actual.getDescription());
        assertEquals(expected.getReleaseDate(), actual.getReleaseDate());
        assertEquals(expected.getDuration(), actual.getDuration());
    }
}
