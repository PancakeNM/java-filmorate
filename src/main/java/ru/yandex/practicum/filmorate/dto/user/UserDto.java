package ru.yandex.practicum.filmorate.dto.user;

import lombok.Builder;
import lombok.Data;
import ru.yandex.practicum.filmorate.model.FriendshipStatus;

import java.time.LocalDate;

@Data
@Builder
public class UserDto {
    private Long id;
    private String email;
    private String login;
    private String name;
    private LocalDate birthday;
    private FriendshipStatus friendshipStatus;
}
