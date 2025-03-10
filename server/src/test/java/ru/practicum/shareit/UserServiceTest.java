package ru.practicum.shareit;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.practicum.shareit.exception.InternalServerException;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.user.*;

import java.util.Collections;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {
    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserServiceImpl userService;

    private UserDto userDto;
    private User user;

    @BeforeEach
    void setUp() {
        user = new User(1L, "Пётр", "fjk@mail.ru");
        userDto = UserMapper.toUserDto(user);
    }

    @Test
    void addUser_ShouldReturnUserDto_WhenEmailIsValid() {
        when(userRepository.findAll()).thenReturn(Collections.emptyList());
        when(userRepository.save(any())).thenReturn(user);

        UserDto result = userService.addUser(userDto);

        assertNotNull(result);
        assertEquals(userDto.getId(), result.getId());
        assertEquals(userDto.getEmail(), result.getEmail());
    }

    @Test
    void addUser_ShouldThrowInternalServerException_WhenEmailIsEmpty() {
        userDto.setEmail("");

        InternalServerException exception = assertThrows(InternalServerException.class, () -> {
            userService.addUser(userDto);
        });

        assertEquals("Отсутствует email", exception.getMessage());
    }


    @Test
    void updateUser_ShouldReturnUpdatedUserDto_WhenUserExists() {
        when(userRepository.findById(anyLong())).thenReturn(Optional.of(user));
        when(userRepository.save(any())).thenReturn(user);
        userService.addUser(userDto);

        UserDto updatedUserDto = new UserDto(user.getId(), "Иван", "ivan@mail.ru");
        UserDto result = userService.updateUser(user.getId(), updatedUserDto);

        assertNotNull(result);
        assertEquals(updatedUserDto.getId(), result.getId());
        assertEquals(updatedUserDto.getEmail(), result.getEmail());
    }

    @Test
    void updateUser_ShouldThrowNotFoundException_WhenUserDoesNotExist() {
        when(userRepository.findById(anyLong())).thenReturn(Optional.empty());

        NotFoundException exception = assertThrows(NotFoundException.class, () -> {
            userService.updateUser(user.getId(), userDto);
        });

        assertEquals("User not found", exception.getMessage());
    }

    @Test
    void getUserById_ShouldReturnUserDto_WhenUserExists() {
        when(userRepository.findById(anyLong())).thenReturn(Optional.of(user));

        UserDto result = userService.getUserById(user.getId());

        assertNotNull(result);
        assertEquals(user.getId(), result.getId());
        assertEquals(user.getEmail(), result.getEmail());
    }

    @Test
    void getUserById_ShouldThrowNotFoundException_WhenUserDoesNotExist() {
        when(userRepository.findById(anyLong())).thenReturn(Optional.empty());

        NotFoundException exception = assertThrows(NotFoundException.class, () -> {
            userService.getUserById(2L);
        });

        assertEquals("User not found", exception.getMessage());
    }

    @Test
    void deleteUser_WhenUserExists() {
        when(userRepository.findById(anyLong())).thenReturn(Optional.of(user));

        userService.deleteUser(user.getId());

        verify(userRepository).findById(user.getId());
        verify(userRepository).delete(user);
    }
}
