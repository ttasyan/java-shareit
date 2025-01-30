package ru.practicum.shareit.user;

import org.springframework.stereotype.Component;

@Component
public class UserMapper {
    private static Long currentId = 1L;

    public static UserDto toUserDto(User user) {
        return new UserDto(
                user.getId(),
                user.getName(),
                user.getEmail()
        );
    }

    public static User fromUserDto(UserDto user) {
        return new User(
                currentId++,
                user.getName(),
                user.getEmail()
        );
    }
}
