package ru.practicum.shareit.user;

public interface UserService {
    UserDto addUser(UserDto userDto);

    UserDto updateUser(long userId, UserDto userDto);

    UserDto getUserById(long userId);

    void deleteUser(long userId);

}
