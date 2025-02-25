package ru.practicum.shareit.user;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exception.InternalServerException;
import ru.practicum.shareit.exception.NotFoundException;


@Service
@Slf4j
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;

    @Override
    public UserDto addUser(UserDto userDto) {
        if (userDto.getEmail().isEmpty() || userDto.getEmail().isBlank()) {
            log.error("Отсутствует email");
            throw new InternalServerException("Отсутствует email");
        }
        emailDuplicate(userDto);
        User user = UserMapper.fromUserDto(userDto);
        log.info("Юзер с id {} добавлен", user.getId());
        return UserMapper.toUserDto(userRepository.save(user));
    }

    @Override
    public UserDto updateUser(long userId, UserDto userDto) {
        emailDuplicate(userDto);
        User user = userRepository.findById(userId).orElseThrow(() -> new NotFoundException("User not found"));
        if (userDto.getEmail() != null) {
            user.setEmail(userDto.getEmail());
        }
        if (userDto.getName() != null) {
            user.setName(userDto.getName());
        }
        log.info("Юзер с id {} обновлен", user.getId());
        return UserMapper.toUserDto(userRepository.save(user));


    }

    @Override
    public UserDto getUserById(long userId) {
        return UserMapper.toUserDto(userRepository.findById(userId).orElseThrow(() -> new NotFoundException("User not found")));
    }

    @Override
    public void deleteUser(long userId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new NotFoundException("User not found"));
        userRepository.delete(user);
        log.info("Юзер с id {} удален", userId);
    }

    private void emailDuplicate(UserDto userDto) {
        if (!userRepository.findAll().stream()
                .filter(user1 -> user1.getEmail().equals(userDto.getEmail())).toList().isEmpty()) {
            log.error("Email {} уже используется", userDto.getEmail());
            throw new InternalServerException("Email уже используется");
        }
    }
}
