package ru.practicum.shareit;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.user.UserDto;
import ru.practicum.shareit.user.UserService;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;

@Transactional
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@SpringBootTest(classes = ShareItApp.class, webEnvironment = SpringBootTest.WebEnvironment.NONE)
public class UserServiceImplTest {
    private final UserService service;

    @Test
    void saveUser() {
        UserDto userDto = new UserDto(1, "Пётр", "fjk@mail.ru");

        service.addUser(userDto);

        assertThat(userDto.getId(), notNullValue());
        assertThat(userDto.getName(), equalTo("Пётр"));
        assertThat(userDto.getEmail(), equalTo("fjk@mail.ru"));

    }
}
