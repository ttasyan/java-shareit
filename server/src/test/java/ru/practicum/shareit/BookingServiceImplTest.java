package ru.practicum.shareit;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.booking.Booking;
import ru.practicum.shareit.booking.BookingRepository;
import ru.practicum.shareit.item.ItemService;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.UserRepository;

import java.util.ArrayList;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;


@Transactional
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@SpringBootTest(classes = ShareItApp.class, webEnvironment = SpringBootTest.WebEnvironment.NONE)
public class BookingServiceImplTest {
    private final BookingRepository repository;
    private final ItemService itemService;
    private final UserRepository userRepository;


    @Test
    void saveBooking() {
        long userId = 1L;
        userRepository.save(new User(userId, "f", "f@mail.ru"));
        ItemDto itemDto = new ItemDto(1, "Пётр", "ygi", true,
                null, null, userId, new ArrayList<>(), null);
        itemService.addItem(userId, itemDto);
        Booking booking1 = new Booking(1, null, null, null, null, Booking.StatusType.WAITING);
        Booking booking2 = repository.save(booking1);

        assertThat(booking2.getId(), notNullValue());
        assertThat(booking2.getItem().getId(), equalTo(itemDto.getId()));
        assertThat(booking2.getStatus(), equalTo("WAITING"));

    }
}
