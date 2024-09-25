package ru.yandex.practicum.filmorate.service.user;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserService {

    private final UserStorage userStorage;

    public Collection<User> getAll() {
        return userStorage.getAll();
    }

    public User create(User newUser) {
        return userStorage.create(newUser);
    }

    public User update(User newUser) {
        return userStorage.update(newUser);
    }

    public User getUserById(Long id) {
        return userStorage.getById(id);
    }

    public User addFriend(Long userId, Long friendId) {
        getUserById(userId).addFriend(friendId);
        getUserById(friendId).addFriend(userId);
        return getUserById(userId);
    }

    public void removeFriend(Long userId, Long friendId) {
        getUserById(userId).removeFriend(friendId);
        getUserById(friendId).removeFriend(userId);
    }

    public List<User> getFriends(Long id) {
        User user = getUserById(id);
        return user.getFriends().stream()
                .map(this::getUserById)
                .toList();
    }

    public List<User> getCommonFriends(Long userId, Long otherId) {
        User user = getUserById(userId);
        User other = getUserById(otherId);
        List<User> userFriends = user.getFriends().stream()
                .map(this::getUserById)
                .toList();
        List<User> otherFriends = other.getFriends().stream()
                .map(this::getUserById)
                .toList();
        List<User> common = new ArrayList<>(userFriends);
        common.retainAll(otherFriends);
        return common;
    }
}
