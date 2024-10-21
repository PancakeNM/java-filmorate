package ru.yandex.practicum.filmorate.storage.mpa;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.dal.BaseRepository;
import ru.yandex.practicum.filmorate.model.Mpa;

import java.util.Collection;
import java.util.Optional;

@Component
public class MpaDbStorage extends BaseRepository<Mpa> implements MpaStorage {
    private static final String MPA_QUERY = "select * from mpa";

    public MpaDbStorage(JdbcTemplate jdbc, RowMapper<Mpa> mapper) {
        super(jdbc, mapper);
    }

    @Override
    public Optional<Mpa> getMpaById(Long mpaId) {
        return findOne(MPA_QUERY.concat(" WHERE id = ?"), mpaId);
    }

    @Override
    public Collection<Mpa> getAllMpa() {
        return findMany(MPA_QUERY.concat(" ORDER BY id ASC"));
    }
}
