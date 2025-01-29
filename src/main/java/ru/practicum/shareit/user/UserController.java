package ru.practicum.shareit.user;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

/**
 * TODO Sprint add-controllers.
 */
@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @PostMapping
    public UserDto addUser( @Valid @RequestBody UserDto userDto) {
        return userService.addUser(userDto);
    }

    @PatchMapping("/{userId}")
    public UserDto updateUser(@PathVariable long userId, @Valid @RequestBody UserDto userDto) {
        return userService.updateUser(userId, userDto);
    }

    @GetMapping("/{userId}")
    public UserDto getUserById(@PathVariable long userId) {
        return userService.getUserById(userId);
    }

    @DeleteMapping("/{userId}")
    public void deleteUser(@PathVariable long userId) {
        userService.deleteUser(userId);
    }
}
