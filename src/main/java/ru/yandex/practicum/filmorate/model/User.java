package ru.yandex.practicum.filmorate.model;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.Instant;

@Data
@EqualsAndHashCode(of = "id")
public class User {
    private int id;
    private String email;
    private String login;
    private String name;
    private Instant birthday;
}
