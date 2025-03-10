package ru.practicum.shareit;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import ru.practicum.shareit.request.ItemRequestController;
import ru.practicum.shareit.request.ItemRequestService;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.user.User;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@ExtendWith(MockitoExtension.class)
public class ItemRequestControllerTest {
    @Mock
    private ItemRequestService service;

    @InjectMocks
    private ItemRequestController controller;

    private MockMvc mvc;

    private ItemRequestDto requestDto;

    private User user;

    private final ObjectMapper mapper = new ObjectMapper();

    @BeforeEach
    void setUp() {
        mvc = MockMvcBuilders.standaloneSetup(controller).build();
        user = new User(1L, "dfg", "glew@mail.ru");
        requestDto = new ItemRequestDto(1L, "kgnkk", null, null, new ArrayList<>());
    }

    @Test
    void addRequest_Ok() throws Exception {
        when(service.addRequest(anyLong(), any()))
                .thenReturn(requestDto);
        mvc.perform(post("/requests")
                        .header("X-Sharer-User-Id", user.getId())
                        .content(mapper.writeValueAsString(requestDto))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(requestDto.getId()), long.class))
                .andExpect(jsonPath("$.description", is(requestDto.getDescription())))
                .andExpect(jsonPath("$.requestor", is(requestDto.getRequestor())))
                .andExpect(jsonPath("$.created", is(requestDto.getCreated())))
                .andExpect(jsonPath("$.items", is(requestDto.getItems())));
    }

    @Test
    void getById_Ok() throws Exception {
        when(service.findById(anyLong()))
                .thenReturn(requestDto);
        mvc.perform(get("/requests/" + requestDto.getId())
                        .header("X-Sharer-User-Id", user.getId())
                        .content(mapper.writeValueAsString(requestDto))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(requestDto.getId()), long.class))
                .andExpect(jsonPath("$.description", is(requestDto.getDescription())))
                .andExpect(jsonPath("$.requestor", is(requestDto.getRequestor())))
                .andExpect(jsonPath("$.created", is(requestDto.getCreated())))
                .andExpect(jsonPath("$.items", is(requestDto.getItems())));
    }

    @Test
    void get_Ok() throws Exception {
        when(service.get(anyLong()))
                .thenReturn(List.of(requestDto));
        mvc.perform(get("/requests")
                        .header("X-Sharer-User-Id", user.getId())
                        .content(mapper.writeValueAsString(requestDto))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id", is(requestDto.getId()), long.class))
                .andExpect(jsonPath("$[0].description", is(requestDto.getDescription())))
                .andExpect(jsonPath("$[0].requestor", is(requestDto.getRequestor())))
                .andExpect(jsonPath("$[0].created", is(requestDto.getCreated())))
                .andExpect(jsonPath("$[0].items", is(requestDto.getItems())));
    }

    @Test
    void getAll_Ok() throws Exception {
        when(service.getAll(anyLong()))
                .thenReturn(List.of(requestDto));
        mvc.perform(get("/requests/all")
                        .header("X-Sharer-User-Id", user.getId())
                        .content(mapper.writeValueAsString(requestDto))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id", is(requestDto.getId()), long.class))
                .andExpect(jsonPath("$[0].description", is(requestDto.getDescription())))
                .andExpect(jsonPath("$[0].requestor", is(requestDto.getRequestor())))
                .andExpect(jsonPath("$[0].created", is(requestDto.getCreated())))
                .andExpect(jsonPath("$[0].items", is(requestDto.getItems())));
    }
}

