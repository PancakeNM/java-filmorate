package ru.yandex.practicum.filmorate.storage.user;

import ru.yandex.practicum.filmorate.model.User;

import java.util.Collection;

public interface UserStorage {

    Collection<User> getAll();

    User create(User newUser);

    User update(User newUser);

    User getById(Long userId);

    User addFriend(Long userId, Long friendId);

    void removeFriend(Long userId, Long friendId);

    Collection<User> getFriends(Long id);

    Collection<User> getCommonFriends(Long userId, Long otherId);
}
