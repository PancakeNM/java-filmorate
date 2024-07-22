package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.yandex.practicum.filmorate.exception.ConditionsNotMetException;
import ru.yandex.practicum.filmorate.exception.DuplicateDataException;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/users")
@Slf4j
public class UserController {
    private final Map<Long, User> users = new HashMap<>();

    @GetMapping
    public Collection<User> getAll() {
        return users.values();
    }

    @PostMapping
    public User create(@RequestBody User newUser) {
        if (newUser.getEmail() == null || newUser.getEmail().isBlank()) {
            log.warn("Email is empty");
            throw new ConditionsNotMetException("Имейл не должен быть пустым");
        } else if (!newUser.getEmail().contains("@")) {
            log.warn("Wrong email format (should contain '@')");
            throw new ConditionsNotMetException("Неверный формат имейла (имейл должен содержать символ '@')");
        } else if (newUser.getLogin() == null || newUser.getLogin().isBlank()) {
            log.warn("Login is empty");
            throw new ConditionsNotMetException("Логин не может быть пустым");
        } else if (newUser.getLogin().contains(" ")) {
            log.warn("Wrong login format (should not contain spaces)");
            throw new ConditionsNotMetException("Неверный формат логина (логин не может содержать пробелы)");
        } else if (newUser.getBirthday().isAfter(LocalDate.now())) {
            log.warn("User birthday cannot be in the future");
            throw new ConditionsNotMetException("Дата рождения пользователя не может быть в будущем");
        }
        for (User user: users.values()) {
            if (user.getEmail().equals(newUser.getEmail())) {
                log.warn("User with email {} already exists", newUser.getEmail());
                throw new DuplicateDataException("Пользователь с имейлом " + newUser.getEmail() + " уже существует");
            }
        }
        if (newUser.getName() == null || newUser.getName().isBlank()) {
            log.info("User name is empty, login will be used instead");
            newUser.setName(newUser.getLogin());
        }
        log.info("Generating new id");
        newUser.setId(generateNewId());
        users.put(newUser.getId(), newUser);
        log.info("New user added");
        return newUser;
    }

    @PutMapping
    public User update(@RequestBody User newUser) {
        if (newUser.getId() == null) {
            log.warn("User id is not stated");
            throw new ConditionsNotMetException("Не указан id обновляемого пользователя");
        }
        if (users.containsKey(newUser.getId())) {
            User oldUser = users.get(newUser.getId());
            if (!oldUser.getEmail().equals(newUser.getEmail())) {
                for (User user : users.values()) {
                    if (user.getEmail().equals(newUser.getEmail())) {
                        log.warn("User with email {} is already exist", newUser.getEmail());
                        throw new DuplicateDataException("Пльхователь с имейлом = " + newUser.getEmail() + " уже существует");
                    }
                }
            }
            log.info("User updated");
            return userUpdater(oldUser, newUser);
        }
        log.warn("User with id {} not found", newUser.getId());
        throw new NotFoundException("Пользователь с id = " + newUser.getId() + " не найден");
    }

    private User userUpdater(User oldUser, User newUser) {
        if (newUser.getEmail() != null) {
            log.info("User email updated");
            oldUser.setEmail(newUser.getEmail());
        }
        if (newUser.getName() != null) {
            log.info("User name update");
            oldUser.setName(newUser.getName());
        }
        if (newUser.getLogin() != null) {
            log.info("User login updated");
            oldUser.setLogin(newUser.getLogin());
        }
        if (newUser.getBirthday() != null) {
            log.info("User birthday updated");
            oldUser.setBirthday(newUser.getBirthday());
        }
        return oldUser;
    }

    private Long generateNewId() {
        long currentMaxId = users.keySet()
                .stream()
                .mapToLong(id -> id)
                .max()
                .orElse(0);
        return ++currentMaxId;
    }
}
