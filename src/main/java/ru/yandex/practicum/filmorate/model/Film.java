package ru.yandex.practicum.filmorate.model;

import lombok.Data;
import lombok.EqualsAndHashCode;
import ru.yandex.practicum.filmorate.exception.NotFoundException;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

/**
 * Film.
 */
@Data
@EqualsAndHashCode(of = "name")
public class Film {
    private Long id;
    private String name;
    private String description;
    private LocalDate releaseDate;
    private Integer duration;
    private Set<Long> likes = new HashSet<>();
    private String genre;
    private String mpa;

    public void addLike(Long userId) {
        likes.add(userId);
    }

    public void removeLike(Long userId) {
        if (!likes.contains(userId)) {
            throw new NotFoundException(String.format("Пользователь с id %s еще не ставил лайк.", userId));
        }
        likes.remove(userId);
    }

    public int getLikesCount() {
        return likes.size();
    }
}
