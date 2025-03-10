package ru.practicum.shareit;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.request.*;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.UserRepository;

import java.time.LocalDateTime;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;


@Transactional
@DataJpaTest
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class ItemRequestServiceImplTest {
    private final UserRepository userRepository;
    private final ItemRequestRepository repository;

    @Test
    void saveItemRequest() {
        long userId = 1L;
        User user = userRepository.save(new User(userId, "f", "f@mail.ru"));
        ItemRequest itemRequest1 = new ItemRequest(1, "des", user, LocalDateTime.now());
        ItemRequest itemRequest2 = repository.save(itemRequest1);

        assertThat(itemRequest2.getId(), notNullValue());
        assertThat(itemRequest2.getDescription(), equalTo("des"));
        assertThat(itemRequest2.getRequestor().getId(), equalTo(userId));
    }

}
