package ru.yandex.practicum.filmorate.model;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;

@Data
@EqualsAndHashCode(of = "email")
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class User {
    Long id;
    String email;
    String login;
    String name;
    LocalDate birthday;
}