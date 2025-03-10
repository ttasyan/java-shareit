package ru.practicum.shareit;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.UserRepository;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;


@Transactional
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@DataJpaTest
public class UserServiceImplTest {
    private final UserRepository repository;

    @Test
    void saveUser() {
        User user = new User(1, "Пётр", "fjk@mail.ru");

        repository.save(user);

        assertThat(user.getId(), notNullValue());
        assertThat(user.getName(), equalTo("Пётр"));
        assertThat(user.getEmail(), equalTo("fjk@mail.ru"));

    }
}
