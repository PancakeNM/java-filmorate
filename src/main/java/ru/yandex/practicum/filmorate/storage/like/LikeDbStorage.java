package ru.yandex.practicum.filmorate.storage.like;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Like;
import ru.yandex.practicum.filmorate.storage.mappers.like.LikeMapper;

import java.util.Collection;

@Slf4j
@Component
@RequiredArgsConstructor
public class LikeDbStorage implements LikeStorage {

    private final JdbcTemplate jdbc;

    @Override
    public void addLike(Long filmId, Long userId) {
        final String insertQuery = "INSERT INTO likes (film_id, user_id) values (?, ?)";
        final String increaseRateQuery = "UPDATE films SET rate = rate + 1 WHERE id = ?";
        try {
            jdbc.update(insertQuery, filmId, userId);
            jdbc.update(increaseRateQuery, filmId);
        } catch (Exception e) {
            log.error("Error while adding like", e);
        }
    }

    @Override
    public boolean removeLike(Long filmId, Long userId) {
        final String deleteQuery = "DELETE FROM likes WHERE film_id = ? AND user_id = ?";
        final String decreaseRateQuery = "UPDATE films SET rate = rate + 1 WHERE id = ?";
        return ((jdbc.update(deleteQuery, filmId, userId) > 0) && (jdbc.update(decreaseRateQuery, filmId) > 0));
    }

    @Override
    public Collection<Like> getLikesFilmId(Long filmId) {
        final String getLikesByFilmId = "SELECT * FROM likes WHERE film_id = ?";
        return jdbc.query(getLikesByFilmId, new LikeMapper(), filmId);
    }
}
