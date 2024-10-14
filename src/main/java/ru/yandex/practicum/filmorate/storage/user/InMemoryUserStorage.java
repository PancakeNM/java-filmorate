package ru.yandex.practicum.filmorate.storage.user;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.DuplicateDataException;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.User;

import java.util.*;

@Component
@Slf4j
public class InMemoryUserStorage implements UserStorage {

    private final Map<Long, User> users = new HashMap<>();

    @Override
    public Collection<User> getAll() {
        return users.values();
    }

    @Override
    public User create(User newUser) {
        for (User user: users.values()) {
            if (user.getEmail().equals(newUser.getEmail())) {
                log.warn("User with email {} already exists", newUser.getEmail());
                throw new DuplicateDataException(String.format("Пользователь с имейлом %s уже существует",
                        newUser.getEmail()));
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

    @Override
    public User update(User newUser) {
        if (users.containsKey(newUser.getId())) {
            User oldUser = users.get(newUser.getId());
            if (!oldUser.getEmail().equals(newUser.getEmail())) {
                for (User user : users.values()) {
                    if (user.getEmail().equals(newUser.getEmail())) {
                        log.warn("User with email {} is already exist", newUser.getEmail());
                        throw new DuplicateDataException(String.format("Пользователь с имейлом = %s уже существует",
                                newUser.getEmail()));
                    }
                }
            }
            log.info("User updated");
            return userUpdater(oldUser, newUser);
        }
        log.warn("User with id {} not found", newUser.getId());
        throw new NotFoundException(String.format("Пользователь с id = %s не найден", newUser.getId()));
    }

    @Override
    public User getById(Long userId) {
        if (users.containsKey(userId)) {
            return users.get(userId);
        }
        log.warn("User with id {} not found", userId);
        throw new NotFoundException(String.format("Пользователь с id = %s не найден", userId));
    }

    public User addFriend(Long userId, Long friendId) {
        getById(userId).addFriend(friendId);
        getById(friendId).addFriend(userId);
        return getById(userId);
    }

    public void removeFriend(Long userId, Long friendId) {
        getById(userId).removeFriend(friendId);
        getById(friendId).removeFriend(userId);
    }

    public List<User> getFriends(Long id) {
        User user = getById(id);
        return user.getFriends().stream()
                .map(this::getById)
                .toList();
    }

    public List<User> getCommonFriends(Long userId, Long otherId) {
        User user = getById(userId);
        User other = getById(otherId);
        List<User> userFriends = user.getFriends().stream()
                .map(this::getById)
                .toList();
        List<User> otherFriends = other.getFriends().stream()
                .map(this::getById)
                .toList();
        List<User> common = new ArrayList<>(userFriends);
        common.retainAll(otherFriends);
        return common;
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
