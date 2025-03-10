package ru.practicum.shareit;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Sort;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.item.ItemRepository;
import ru.practicum.shareit.request.ItemRequest;
import ru.practicum.shareit.request.ItemRequestRepository;
import ru.practicum.shareit.request.ItemRequestServiceImpl;
import ru.practicum.shareit.request.NewItemRequest;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.user.*;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ItemRequestServiceTest {
    @Mock
    private ItemRequestRepository repository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private ItemRepository itemRepository;

    @Mock
    private UserService userService;

    @InjectMocks
    private ItemRequestServiceImpl service;

    private UserDto userDto;
    private User user;
    private NewItemRequest newItemRequest;
    private ItemRequest itemRequest;

    @BeforeEach
    void setUp() {
        userDto = new UserDto(1L, "Пётр", "fjk@mail.ru");
        userService.addUser(userDto);
        user = UserMapper.fromUserDto(userDto);
        newItemRequest = new NewItemRequest();
        newItemRequest.setDescription("description");
        itemRequest = new ItemRequest(1L, newItemRequest.getDescription(), user, LocalDateTime.now());
    }

    @Test
    void addRequest_ShouldReturnItemRequestDto_WhenUserExists() {
        when(userRepository.findById(anyLong())).thenReturn(Optional.ofNullable(user));
        when(repository.save(any(ItemRequest.class))).thenReturn(itemRequest);

        ItemRequestDto result = service.addRequest(userDto.getId(), newItemRequest);

        assertNotNull(result);
        assertEquals(itemRequest.getId(), result.getId());
        assertEquals(newItemRequest.getDescription(), result.getDescription());
        verify(userRepository).findById(user.getId());
        verify(repository).save(any(ItemRequest.class));
    }

    @Test
    void addRequest_ShouldThrowNotFoundException_WhenUserDoesNotExist() {
        when(userRepository.findById(user.getId())).thenReturn(Optional.empty());

        NotFoundException exception = assertThrows(NotFoundException.class, () -> {
            service.addRequest(user.getId(), newItemRequest);
        });

        assertEquals("Юзер не найден", exception.getMessage());
        verify(userRepository).findById(user.getId());
        verify(repository, never()).save(any(ItemRequest.class));
    }

    @Test
    void get_ShouldReturnListOfItemRequestDto_WhenUserExists() {
        when(userRepository.findById(user.getId())).thenReturn(Optional.of(user));
        when(repository.findAllByRequestorId(user.getId(), Sort.by(Sort.Direction.ASC, "created")))
                .thenReturn(Collections.singletonList(itemRequest));

        List<ItemRequestDto> result = service.get(user.getId());

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(itemRequest.getId(), result.get(0).getId());
        verify(userRepository).findById(user.getId());
        verify(repository).findAllByRequestorId(user.getId(), Sort.by(Sort.Direction.ASC, "created"));
    }

    @Test
    void get_ShouldThrowNotFoundException_WhenUserDoesNotExist() {
        when(userRepository.findById(user.getId())).thenReturn(Optional.empty());

        NotFoundException exception = assertThrows(NotFoundException.class, () -> {
            service.get(user.getId());
        });

        assertEquals("Юзер не найден", exception.getMessage());
        verify(userRepository).findById(user.getId());
        verify(repository, never()).findAllByRequestorId(anyLong(), any());
    }

    @Test
    void findById_ShouldReturnItemRequestDto_WhenRequestExists() {
        when(repository.findById(itemRequest.getId())).thenReturn(Optional.of(itemRequest));
        when(itemRepository.findByRequestId(itemRequest.getId())).thenReturn(Collections.emptyList());

        ItemRequestDto result = service.findById(itemRequest.getId());

        assertNotNull(result);
        assertEquals(itemRequest.getId(), result.getId());
        verify(repository).findById(itemRequest.getId());
        verify(itemRepository).findByRequestId(itemRequest.getId());
    }

    @Test
    void getAll_ShouldReturnListOfItemRequestDto_WhenUserExists() {
        when(userRepository.findById(user.getId())).thenReturn(Optional.of(user));
        when(repository.findByRequestorIdNot(user.getId(), Sort.by(Sort.Direction.ASC, "created")))
                .thenReturn(Collections.singletonList(itemRequest));

        List<ItemRequestDto> result = service.getAll(user.getId());

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(itemRequest.getId(), result.get(0).getId());
        verify(userRepository).findById(user.getId());
        verify(repository).findByRequestorIdNot(user.getId(), Sort.by(Sort.Direction.ASC, "created"));
    }

    @Test
    void getAll_ShouldThrowNotFoundException_WhenUserDoesNotExist() {
        when(userRepository.findById(user.getId())).thenReturn(Optional.empty());

        NotFoundException exception = assertThrows(NotFoundException.class, () -> {
            service.getAll(user.getId());
        });

        assertEquals("Юзер не найден", exception.getMessage());
        verify(userRepository).findById(user.getId());
        verify(repository, never()).findByRequestorIdNot(anyLong(), any());
    }
}
