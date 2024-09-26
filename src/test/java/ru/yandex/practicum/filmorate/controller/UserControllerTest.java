package ru.yandex.practicum.filmorate.controller;


import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.exception.ConditionsNotMetException;
import ru.yandex.practicum.filmorate.exception.DuplicateDataException;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.user.UserService;
import ru.yandex.practicum.filmorate.storage.user.InMemoryUserStorage;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class UserControllerTest {
    UserController controller;
    UserService service;
    UserStorage storage;
    User user1;
    User user2;

    @BeforeEach
    public void beforeEach() {
        storage = new InMemoryUserStorage();
        service = new UserService(storage);
        controller = new UserController(service);
        user1 = new User();
        user1.setName("TestName1");
        user1.setLogin("TestLogin1");
        user1.setEmail("testEmail1@test.com");
        user1.setBirthday(LocalDate.of(2000, 1, 1));
        user2 = new User();
        user2.setName("TestName2");
        user2.setLogin("TestLogin2");
        user2.setEmail("testEmail2@test.com");
        user2.setBirthday(LocalDate.of(2001, 1, 1));
    }

    @Test
    public void getAllShouldReturnAllUsers() {
        List<User> expected = new ArrayList<>();
        expected.add(user1);
        expected.add(user2);
        controller.create(user1);
        controller.create(user2);

        assertEquals(expected, controller.getAll().stream().toList());
    }

    @Test
    public void createShouldThrowConditionsNotMetExceptionIfLoginIsNull() {
        user1.setLogin(null);
        Class<?> expectedClass = ConditionsNotMetException.class;
        Class<?> actualClass = null;
        try {
            controller.create(user1);
        } catch (RuntimeException e) {
            actualClass = e.getClass();
        }
        assertEquals(expectedClass, actualClass);
    }

    @Test
    public void createShouldThrowConditionsNotMetExceptionIfLoginIsBlank() {
        user1.setLogin("  ");
        Class<?> expectedClass = ConditionsNotMetException.class;
        Class<?> actualClass = null;
        try {
            controller.create(user1);
        } catch (RuntimeException e) {
            actualClass = e.getClass();
        }
        assertEquals(expectedClass, actualClass);
    }

    @Test
    public void createShouldThrowConditionsNotMetExceptionIfBirthdayIsInFuture() {
        user1.setBirthday(LocalDate.now().plusYears(1));
        Class<?> expectedClass = ConditionsNotMetException.class;
        Class<?> actualClass = null;
        try {
            controller.create(user1);
        } catch (RuntimeException e) {
            actualClass = e.getClass();
        }
        assertEquals(expectedClass, actualClass);
    }

    @Test
    public void createShouldThrowConditionsNotMetExceptionIfEmailIsNull() {
        user1.setEmail(null);
        Class<?> expectedClass = ConditionsNotMetException.class;
        Class<?> actualClass = null;
        try {
            controller.create(user1);
        } catch (RuntimeException e) {
            actualClass = e.getClass();
        }
        assertEquals(expectedClass, actualClass);
    }

    @Test
    public void createShouldThrowConditionsNotMetExceptionIfNewUserEmailIsWrongFormat() {
        user2.setEmail("testEmail.com");
        Class<?> expectedClass = ConditionsNotMetException.class;
        Class<?> actualClass = null;
        controller.create(user1);
        try {
            controller.create(user2);
        } catch (RuntimeException e) {
            actualClass = e.getClass();
        }
        assertEquals(expectedClass, actualClass);
    }

    @Test
    public void createShouldThrowConditionsNotMetExceptionIfNewUserLoginIsWrongFormat() {
        user2.setLogin("Test login");
        Class<?> expectedClass = ConditionsNotMetException.class;
        Class<?> actualClass = null;
        controller.create(user1);
        try {
            controller.create(user2);
        } catch (RuntimeException e) {
            actualClass = e.getClass();
        }
        assertEquals(expectedClass, actualClass);
    }

    @Test
    public void createShouldThrowDuplicateDataExceptionIfNewUserEmailAlreadyExists() {
        user2.setEmail(user1.getEmail());
        Class<?> expectedClass = DuplicateDataException.class;
        Class<?> actualClass = null;
        controller.create(user1);
        try {
            controller.create(user2);
        } catch (RuntimeException e) {
            actualClass = e.getClass();
        }
        assertEquals(expectedClass, actualClass);
    }

    @Test
    public void createShouldReplaceNameWithLoginIfNameIsEmpty() {
        user1.setName(null);
        controller.create(user1);
        assertEquals(user1.getLogin(), user1.getName());
    }

    @Test
    public void updateShouldThrowNotFoundExceptionIfThereIsNoSuchId() {
        controller.create(user1);
        controller.create(user2);
        User newUser = new User();
        newUser.setId(3L);
        Class<?> expectedClass = NotFoundException.class;
        Class<?> actualClass = null;
        try {
            controller.update(newUser);
        } catch (RuntimeException e) {
            actualClass = e.getClass();
        }
        assertEquals(expectedClass, actualClass);
    }

    @Test
    public void updateShouldThrowConditionsNotMetExceptionIfNewUserIdIsNull() {
        controller.create(user1);
        controller.create(user2);
        User newUser = new User();
        newUser.setName("UpdateTestUserName");
        Class<?> expectedClass = ConditionsNotMetException.class;
        Class<?> actualClass = null;
        try {
            controller.update(newUser);
        } catch (RuntimeException e) {
            actualClass = e.getClass();
        }
        assertEquals(expectedClass, actualClass);
    }

    @Test
    public void createShouldReturnCreatedUser() {
        User expected = new User();
        expected.setId(1L);
        expected.setName(user1.getName());
        expected.setLogin(user1.getLogin());
        expected.setEmail(user1.getEmail());
        expected.setBirthday(user1.getBirthday());

        User actual = controller.create(user1);

        userFieldsCheck(expected, actual);
    }

    @Test
    public void updateShouldReturnUpdatedUser() {
        controller.create(user1);
        User newUser = new User();
        newUser.setId(1L);
        newUser.setName("UpdateTestName");

        User expected = new User();
        expected.setId(1L);
        expected.setName(newUser.getName());
        expected.setLogin(user1.getLogin());
        expected.setBirthday(user1.getBirthday());
        expected.setEmail(user1.getEmail());

        User actual = controller.update(newUser);

        userFieldsCheck(expected, actual);
    }

    @Test
    public void updateShouldNotUpdateNameWhenNameIsNull() {
        controller.create(user1);
        User newUser = new User();
        newUser.setId(1L);
        newUser.setLogin("UpdateTestLogin");
        newUser.setBirthday(LocalDate.of(2002, 1, 1));
        newUser.setEmail("UpdateTestEmail@test.com");

        User expected = new User();
        expected.setId(1L);
        expected.setName(user1.getName());
        expected.setLogin(newUser.getLogin());
        expected.setBirthday(newUser.getBirthday());
        expected.setEmail(newUser.getEmail());

        User actual = controller.update(newUser);

        userFieldsCheck(expected, actual);
    }

    @Test
    public void updateShouldNotUpdateLoginWhenLoginIsNull() {
        controller.create(user1);
        User newUser = new User();
        newUser.setId(1L);
        newUser.setName("UpdateTestName");
        newUser.setBirthday(LocalDate.of(2002, 1, 1));
        newUser.setEmail("UpdateTestEmail@test.com");

        User expected = new User();
        expected.setId(1L);
        expected.setName(newUser.getName());
        expected.setLogin(user1.getLogin());
        expected.setEmail(newUser.getEmail());
        expected.setBirthday(newUser.getBirthday());

        User actual = controller.update(newUser);

        userFieldsCheck(expected, actual);
    }

    @Test
    public void updateShouldNotUpdateBirthdayWhenBirthdayIsNull() {
        controller.create(user1);
        User newUser = new User();
        newUser.setId(1L);
        newUser.setName("UpdateTestName");
        newUser.setLogin("UpdateTestLogin");
        newUser.setEmail("UpdateTestEmail@test.com");

        User expected = new User();
        expected.setId(1L);
        expected.setName(newUser.getName());
        expected.setLogin(newUser.getLogin());
        expected.setBirthday(user1.getBirthday());
        expected.setEmail(newUser.getEmail());

        User actual = controller.update(newUser);

        userFieldsCheck(expected, actual);
    }

    @Test
    public void updateShouldNotUpdateEmailWhenEmailIsNull() {
        controller.create(user1);
        User newUser = new User();
        newUser.setId(1L);
        newUser.setName("UpdateTestName");
        newUser.setLogin("UpdateTestLogin");
        newUser.setBirthday(LocalDate.of(2002, 1, 1));

        User expected = new User();
        expected.setId(1L);
        expected.setName(newUser.getName());
        expected.setLogin(newUser.getLogin());
        expected.setEmail(user1.getEmail());
        expected.setBirthday(newUser.getBirthday());

        User actual = controller.update(newUser);

        userFieldsCheck(expected, actual);
    }

    @Test
    public void updateShouldThrowDuplicateDataExceptionIfNewUserEmailIsAlreadyExists() {
        controller.create(user1);
        controller.create(user2);
        User newUser = new User();
        newUser.setEmail(user1.getEmail());
        newUser.setId(user2.getId());
        Class<?> expectedClass = DuplicateDataException.class;
        Class<?> actualClass = null;
        try {
            controller.update(newUser);
        } catch (RuntimeException e) {
            actualClass = e.getClass();
        }
        assertEquals(expectedClass, actualClass);
    }

    private void userFieldsCheck(User expected, User actual) {
        assertEquals(expected.getId(), actual.getId());
        assertEquals(expected.getName(), actual.getName());
        assertEquals(expected.getLogin(), actual.getLogin());
        assertEquals(expected.getEmail(), actual.getEmail());
        assertEquals(expected.getBirthday(), actual.getBirthday());
    }
}
