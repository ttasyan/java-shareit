package ru.practicum.shareit;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.booking.BookingService;
import ru.practicum.shareit.item.ItemService;
import ru.practicum.shareit.user.UserService;



@Transactional
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@SpringBootTest(classes = ShareItApp.class, webEnvironment = SpringBootTest.WebEnvironment.NONE)

public class BookingServiceImplTest {
    private final BookingService service;
    private final UserService userService;
    private final ItemService itemService;

//    @Test
//    void saveBooking() {
//        long userId = 1L;
//        UserDto userDto = new UserDto(1, "Пётр", "fjk@mail.ru");
//        userService.addUser(userDto);
//        ItemDto itemDto = new ItemDto(1, "Пётр", "ygi", true,
//                null, null, userId, new ArrayList<>(), null);
//        itemService.addItem(userId, itemDto);
//        NewBookingRequest request = new NewBookingRequest();
//        request.setItemId(itemDto.getId());
//        request.setStart(LocalDateTime.now());
//        request.setEnd(LocalDateTime.now().plusHours(1));
//        BookingDto bookingDto = service.addBooking(userId, request);
//
//        assertThat(bookingDto.getId(), notNullValue());
//        assertThat(bookingDto.getBooker().getId(), equalTo(userId));
//        assertThat(bookingDto.getItem().getId(), equalTo(itemDto.getId()));
//        assertThat(bookingDto.getStatus(), equalTo("WAITING"));
//
//    }
}
