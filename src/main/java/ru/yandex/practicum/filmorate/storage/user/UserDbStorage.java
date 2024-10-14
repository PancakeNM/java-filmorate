package ru.yandex.practicum.filmorate.storage.user;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.User;

import java.util.Collection;
import java.util.List;

@Component
@Slf4j
public class UserDbStorage implements UserStorage {

    @Override
    public Collection<User> getAll() {
        return null;
    }

    @Override
    public User create(User newUser) {
        return null;
    }

    @Override
    public User update(User newUser) {
        return null;
    }

    @Override
    public User getById(Long userId) {
        return null;
    }

    @Override
    public User addFriend(Long userId, Long friendId) {
        return null;
    }

    @Override
    public void removeFriend(Long userId, Long friendId) {

    }

    @Override
    public List<User> getFriends(Long id) {
        return null;
    }

    @Override
    public List<User> getCommonFriends(Long userId, Long otherId) {
        return null;
    }
}
