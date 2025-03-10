package ru.practicum.shareit;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.booking.Booking;
import ru.practicum.shareit.booking.BookingRepository;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.UserRepository;


import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;


@Transactional
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@SpringBootTest(classes = ShareItApp.class, webEnvironment = SpringBootTest.WebEnvironment.NONE)
public class BookingServiceImplTest {
    private final BookingRepository repository;
    private final UserRepository userRepository;


    @Test
    void saveBooking() {
        long userId = 1L;
        User user = userRepository.save(new User(userId, "f", "f@mail.ru"));
        Booking booking1 = new Booking(2L, null, null, null, user, Booking.StatusType.WAITING);
        Booking booking2 = repository.save(booking1);

        assertThat(booking2.getId(), notNullValue());
        assertThat(booking2.getBooker().getId(), equalTo(user.getId()));
        assertThat(booking2.getStatus().toString(), equalTo("WAITING"));

    }
}
