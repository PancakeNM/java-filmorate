package ru.yandex.practicum.filmorate.service.user;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.follow.FollowStorage;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.util.Collection;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserService {

    private final UserStorage userStorage;
    private final FollowStorage followStorage;


    public Collection<User> getAll() {
        return userStorage.getAll();
    }

    public User create(User newUser) {
        if (newUser.getName().isEmpty()) {
            newUser.setName(newUser.getLogin());
        }
        return userStorage.create(newUser);
    }

    public User update(User newUser) {
        return userStorage.update(newUser);
    }

    public User getUserById(Long id) {
        return userStorage.getById(id);
    }

    public void addFriend(Long userId, Long friendId) {
        followStorage.addFriend(userId, friendId);
    }

    public void removeFriend(Long userId, Long friendId) {
        followStorage.deleteFriend(userId, friendId);
    }

    public Collection<User> getFriends(Long id) {
        return userStorage.getFriends(id);
    }

    public Collection<User> getCommonFriends(Long userId, Long otherId) {
        return userStorage.getCommonFriends(userId, otherId);
    }
}
