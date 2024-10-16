package ru.yandex.practicum.filmorate.storage.follow;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class FollowDbStorage implements FollowStorage {
    private final JdbcTemplate jdbc;

    @Override
    public void addFriend(Long userId, Long friendId) {
        try {
            final String sql = "INSERT INTO FOLLOWS (FOLLOWING_USER_ID, FOLLOWED_USER_ID) VALUES (?, ?)";

            jdbc.update(sql, userId, friendId);
        } catch (Exception e) {
            log.error("Error sending friend request: {}", e.getMessage());
        }

    }

    @Override
    public void deleteFriend(Long userId, Long friendId) {

        final String sql = "DELETE FROM follows WHERE FOLLOWING_USER_ID = ? AND FOLLOWED_USER_ID = ?";
        try {
            jdbc.update(sql, userId, friendId);
        } catch (Exception e) {
            log.error("User is not friend, or deletion was not successful");
        }
    }
}
