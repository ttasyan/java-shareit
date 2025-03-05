package ru.practicum.shareit;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import ru.practicum.shareit.booking.BookingController;
import ru.practicum.shareit.booking.BookingService;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.request.ItemRequest;
import ru.practicum.shareit.user.User;



@ExtendWith(MockitoExtension.class)
public class BookingControllerTest {
    @Mock
    private BookingService service;

    @InjectMocks
    private BookingController controller;

    private MockMvc mvc;

    private BookingDto bookingDto;

    private final ObjectMapper mapper = new ObjectMapper();

    @BeforeEach
    void setUp() {
        mvc = MockMvcBuilders.standaloneSetup(controller).build();
        User user = new User(1L, "dfg", "glew@mail.ru");
        Item item = new Item(2L, "f", "fkop", true, null, null,
                user, new ItemRequest());
        bookingDto = new BookingDto(2L, null, null, item, user, "WAITING");
    }
}
