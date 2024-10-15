package ru.yandex.practicum.filmorate.storage.follow;

public interface FollowStorage {
    void addFriend(Long userId, Long friendId);

    void deleteFriend(Long userId, Long friendId);
}