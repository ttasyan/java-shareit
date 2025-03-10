package ru.practicum.shareit;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.item.ItemRepository;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.UserRepository;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;


@Transactional
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@SpringBootTest(classes = ShareItApp.class, webEnvironment = SpringBootTest.WebEnvironment.NONE)
public class ItemServiceImplTest {
    private final ItemRepository repository;
    private final UserRepository userRepository;

    @Test
    void saveItem() {
        long userId = 1;
        userRepository.save(new User(userId, "f", "f@mail.ru"));
        Item item = new Item(1, "Пётр", "ygi", true,
                null, null, null, null);

        repository.save(item);


        assertThat(item.getId(), notNullValue());
        assertThat(item.getName(), equalTo("Пётр"));
        assertThat(item.getDescription(), equalTo("ygi"));

    }
}
