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
import org.springframework.web.bind.annotation.RestController;
import ru.yandex.practicum.filmorate.exception.ConditionsNotMetException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.user.UserService;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/users")
@Slf4j
public class UserController {
    private final UserService service;
    private final String friendsPath = "/{id}/friends";

    @GetMapping
    public Collection<User> getAll() {
        return service.getAll();
    }

    @PostMapping
    public User create(@RequestBody User newUser) {
        validate(newUser);
        return service.create(newUser);
    }

    @PutMapping
    public User update(@RequestBody User newUser) {
        if (newUser.getId() == null) {
            log.warn("User id is not stated");
            throw new ConditionsNotMetException("Не указан id обновляемого пользователя");
        }
        return service.update(newUser);
    }

    @PutMapping(friendsPath + "/{friend-id}")
    public void addFriend(@PathVariable Long id, @PathVariable(name = "friend-id") Long friendId) {
        validate(id, friendId);
        service.addFriend(id, friendId);
    }

    @DeleteMapping(friendsPath + "/{friend-id}")
    public void removeFriend(@PathVariable Long id, @PathVariable(name = "friend-id") Long friendId) {
        validate(id, friendId);
        service.removeFriend(service.getUserById(id), service.getUserById(friendId));
    }

    @GetMapping(friendsPath)
    public List<User> getFriends(@PathVariable Long id) {
        validate(id);
        return new ArrayList<>(service.getFriends(id));
    }

    @GetMapping(friendsPath + "/common/{other-id}")
    public List<User> getCommonFriends(@PathVariable Long id, @PathVariable(name = "other-id") Long otherId) {
        validate(id, otherId);
        return new ArrayList<>(service.getCommonFriends(id, otherId));
    }

    private void validate(User user) {
        if (user.getEmail() == null) {
            log.warn("Email is empty");
            throw new ConditionsNotMetException("Имейл не должен быть пустым");
        } else if (!user.getEmail().contains("@")) {
            log.warn("Wrong email format (should contain '@')");
            throw new ConditionsNotMetException("Неверный формат имейла (имейл должен содержать символ '@')");
        } else if (user.getLogin() == null || user.getLogin().isBlank()) {
            log.warn("Login is empty");
            throw new ConditionsNotMetException("Логин не может быть пустым");
        } else if (user.getLogin().contains(" ")) {
            log.warn("Wrong login format (should not contain spaces)");
            throw new ConditionsNotMetException("Неверный формат логина (логин не может содержать пробелы)");
        } else if (user.getBirthday().isAfter(LocalDate.now())) {
            log.warn("User birthday cannot be in the future");
            throw new ConditionsNotMetException("Дата рождения пользователя не может быть в будущем");
        }
    }

    private void validate(Long id, Long otherId) {
        if (id == null) {
            log.warn("Id is null");
            throw new ConditionsNotMetException("Не указан id пользователя.");
        } else if (otherId == null) {
            log.warn("otherId is null");
            throw new ConditionsNotMetException("Не указан id друга.");
        }
    }

    private void validate(Long id) {
        if (id == null) {
            log.warn("Id is null");
            throw new ConditionsNotMetException("Не указан id пользователя.");
        }
    }
}
