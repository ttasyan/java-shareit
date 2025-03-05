package ru.practicum.shareit;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import ru.practicum.shareit.item.ItemController;
import ru.practicum.shareit.item.ItemService;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.user.User;

import java.util.ArrayList;


@ExtendWith(MockitoExtension.class)
public class ItemControllerTest {
    @Mock
    private ItemService service;

    @InjectMocks
    private ItemController controller;

    private MockMvc mvc;

    private ItemDto itemDto;

    private final ObjectMapper mapper = new ObjectMapper();

    @BeforeEach
    void setUp() {
        mvc = MockMvcBuilders.standaloneSetup(controller).build();
        User user = new User(1L, "dfg", "glew@mail.ru");
        itemDto = new ItemDto(2L, "f", "fkop", true, null, null,
                user.getId(), new ArrayList<>(), null);
    }
}
