package ru.practicum.shareit;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.item.ItemService;
import ru.practicum.shareit.user.UserService;


@Transactional
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@SpringBootTest(classes = ShareItApp.class, webEnvironment = SpringBootTest.WebEnvironment.NONE)
public class ItemServiceImplTest {
    private final UserService userService;
    private final ItemService service;

//    @Test
//    void saveItem() {
//        long userId = 1;
//        UserDto userDto = new UserDto(1, "Пётр", "fjk@mail.ru");
//        userService.addUser(userDto);
//        ItemDto itemDto = new ItemDto(1, "Пётр", "ygi", true,
//                null, null, userId, new ArrayList<>(), null);
//
//        try {
//            service.addItem(userDto.getId(), itemDto);
//        } catch (NotFoundException e) {
//
//        }
//
//        assertThat(itemDto.getId(), notNullValue());
//        assertThat(itemDto.getName(), equalTo("Пётр"));
//        assertThat(itemDto.getDescription(), equalTo("ygi"));
//        assertThat(itemDto.getAvailable(), equalTo(true));
//        assertThat(itemDto.getOwnerId(), equalTo(userId));
//
//    }
}
