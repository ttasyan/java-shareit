package ru.practicum.shareit;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.item.ItemService;
import ru.practicum.shareit.item.dto.ItemDto;

import java.util.ArrayList;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;


@Transactional
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@SpringBootTest(classes = ShareItApp.class, webEnvironment = SpringBootTest.WebEnvironment.NONE)
public class ItemServiceImplTest {
    private final ItemService service;

    @Test
    void saveItem() {
        long userId = 1;
        ItemDto itemDto = new ItemDto(1, "Пётр", "ygi", true,
                null, null, userId, new ArrayList<>(), null);

        service.addItem(userId, itemDto);


        assertThat(itemDto.getId(), notNullValue());
        assertThat(itemDto.getName(), equalTo("Пётр"));
        assertThat(itemDto.getDescription(), equalTo("ygi"));
        assertThat(itemDto.getAvailable(), equalTo(true));
        assertThat(itemDto.getOwnerId(), equalTo(userId));

    }
}
