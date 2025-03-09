package ru.practicum.shareit;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.request.ItemRequestService;
import ru.practicum.shareit.request.NewItemRequest;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.user.UserDto;
import ru.practicum.shareit.user.UserService;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;


@Transactional
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@SpringBootTest(classes = ShareItApp.class, webEnvironment = SpringBootTest.WebEnvironment.NONE)
public class ItemRequestServiceImplTest {
    private final UserService userService;
    private final ItemRequestService service;

    @Test
    void saveItemRequest() {
        UserDto user = new UserDto(1, "Пётр", "fjk@mail.ru");
        userService.addUser(user);
        NewItemRequest itemRequest = new NewItemRequest();
        itemRequest.setDescription("des");
        ItemRequestDto itemRequestDto = service.addRequest(user.getId(), itemRequest);

        assertThat(itemRequestDto.getId(), notNullValue());
        assertThat(itemRequestDto.getDescription(), equalTo("des"));
        assertThat(itemRequestDto.getRequestor().getId(), equalTo(user.getId()));
    }

}
