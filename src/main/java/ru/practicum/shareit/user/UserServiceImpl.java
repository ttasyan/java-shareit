package ru.practicum.shareit.user;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exception.InternalServerException;
import ru.practicum.shareit.exception.NotFoundException;

import java.util.HashMap;
import java.util.Map;

@Service
@Slf4j
public class UserServiceImpl implements UserService {
    private Long currentId = 1L;
    private final Map<Long, User> users = new HashMap<>();

    public UserDto addUser(UserDto userDto) {
        if (userDto.getEmail().isEmpty() || userDto.getEmail().isBlank()) {
            log.error("Отсутствует email");
            throw new InternalServerException("Отсутствует email");
        }
        emailDuplicate(userDto);
        User user = UserMapper.fromUserDto(userDto);
        user.setId(currentId++);
        users.put(user.getId(), user);
        log.info("Юзер с id {} добавлен", user.getId());
        return UserMapper.toUserDto(user);
    }

    public UserDto updateUser(long userId, UserDto userDto) {
        emailDuplicate(userDto);
        User user = users.get(userId);
        if (userDto.getEmail() != null) {
            user.setEmail(userDto.getEmail());
        }
        if (userDto.getName() != null) {
            user.setName(userDto.getName());
        }
        log.info("Юзер с id {} обновлен", user.getId());
        return UserMapper.toUserDto(user);


    }

    public UserDto getUserById(long userId) {
        if (!users.containsKey(userId)) {
            log.error("Юзер не найден");
            throw new NotFoundException("Юзер не найден");
        }
        return UserMapper.toUserDto(users.get(userId));
    }

    public void deleteUser(long userId) {
        users.remove(userId);
        log.info("Юзер с id {} удален", userId);
    }

    private void emailDuplicate(UserDto userDto) {
        if (!users.values().stream()
                .filter(user1 -> user1.getEmail().equals(userDto.getEmail())).toList().isEmpty()) {
            log.error("Email {} уже используется", userDto.getEmail());
            throw new InternalServerException("Email уже используется");
        }
    }
}
