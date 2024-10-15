package ru.yandex.practicum.filmorate.storage.user;

import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.dal.BaseRepository;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.User;

import java.util.Collection;
import java.util.Optional;

@Component
@Slf4j
public class UserDbStorage extends BaseRepository<User> implements UserStorage {

    private static final String USER_QUERY = "SELECT * FROM users";
    private static final String CREATE_QUERY = "INSERT INTO users (name, login, birthday, email) values " +
                                                "(?, ?, ?, ?, ?)";
    private static final String UPDATE_QUERY = "UPDATE users SET name = ?, login = ?, birthday = ?, email = ? " +
                                                "WHERE id = ?";
    private static final String FIND_COMMON_FRIENDS_QUERY = "SELECT * FROM users WHERE id IN " +
            "(SELECT followed_user_id FROM users u JOIN follows fl ON u.id = fl.following_user_id WHERE u.id = ?) " +
            "AND id IN (SELECT followed_user_id FROM users u JOIN follows fl ON " +
            "u.id = fl.following_user_id WHERE u.id = ?)";
    private static final String FIND_USER_FRIENDS_QUERY = "SELECT * FROM users WHERE id IN " +
            "(SELECT fl.followed_user_id FROM users u JOIN follows fl ON u.id = fl.following_user_id WHERE u.id = ?";

    public UserDbStorage(JdbcTemplate jdbc, RowMapper<User> mapper) {
        super(jdbc, mapper);
    }

    @Override
    public Collection<User> getAll() {
        return findMany(USER_QUERY);
    }

    @Override
    public User create(User newUser) {
        Long id = insert(
                CREATE_QUERY,
                newUser.getEmail(),
                newUser.getLogin(),
                newUser.getName(),
                newUser.getBirthday()
        );
        newUser.setId(id);
        return newUser;
    }

    @Override
    public User update(User newUser) {
        update(
                UPDATE_QUERY,
                newUser.getLogin(),
                newUser.getLogin(),
                newUser.getName(),
                newUser.getBirthday()
        );
        return newUser;
    }

    @Override
    public User getById(Long userId) {
        Optional<User> optionalUser = findOne(USER_QUERY.concat("WHERE id = ?"), userId);
        if (optionalUser.isEmpty()) {
            throw new NotFoundException(String.format("Пользователь с id = %s не найден", userId));
        } else {
            return optionalUser.get();
        }
    }

    @Deprecated
    @Override
    public User addFriend(Long userId, Long friendId) {
        return null;
    }

    @Deprecated
    @Override
    public void removeFriend(Long userId, Long friendId) {

    }

    @Override
    public Collection<User> getFriends(Long id) {
        return findMany(FIND_USER_FRIENDS_QUERY, id);
    }

    @Override
    public Collection<User> getCommonFriends(Long userId, Long otherId) {
        return findMany(FIND_COMMON_FRIENDS_QUERY, userId, otherId);
    }
}
