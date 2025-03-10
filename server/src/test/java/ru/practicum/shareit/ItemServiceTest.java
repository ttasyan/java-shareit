package ru.practicum.shareit;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Sort;
import ru.practicum.shareit.booking.Booking;
import ru.practicum.shareit.booking.BookingRepository;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.exception.ValidationException;
import ru.practicum.shareit.item.*;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.request.*;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.UserRepository;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ItemServiceTest {
    @Mock
    private ItemRepository itemRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private BookingRepository bookingRepository;

    @Mock
    private CommentRepository commentRepository;

    @Mock
    private ItemRequestRepository requestRepository;

    @InjectMocks
    private ItemServiceImpl itemService;

    private User user;
    private ItemDto itemDto;
    private Item item;

    @BeforeEach
    void setUp() {
        user = new User(1L, "Пётр", "fjk@mail.ru");
        itemDto = new ItemDto(1L, "Вещь", "Описание", true, null,
                null, user.getId(), null, null);
        item = ItemMapper.fromItemDto(itemDto);
        item.setOwner(user);
    }

    @Test
    void addItem_ShouldReturnItemDto_WhenUserExistsAndItemIsValid() {
        when(userRepository.findById(user.getId())).thenReturn(Optional.of(user));
        when(itemRepository.save(any(Item.class))).thenReturn(item);

        ItemDto result = itemService.addItem(user.getId(), itemDto);

        assertNotNull(result);
        assertEquals(itemDto.getId(), result.getId());
        assertEquals(itemDto.getName(), result.getName());
        verify(userRepository).findById(user.getId());
        verify(itemRepository).save(any(Item.class));
    }

    @Test
    void addItem_ShouldThrowNotFoundException_WhenUserDoesNotExist() {
        when(userRepository.findById(user.getId())).thenReturn(Optional.empty());

        NotFoundException exception = assertThrows(NotFoundException.class, () -> {
            itemService.addItem(user.getId(), itemDto);
        });

        assertEquals("Пользователь не найден", exception.getMessage());
        verify(userRepository).findById(user.getId());
        verify(itemRepository, never()).save(any(Item.class));
    }


    @Test
    void updateItem_ShouldReturnUpdatedItemDto_WhenItemExists() {
        when(userRepository.findById(user.getId())).thenReturn(Optional.of(user));
        when(itemRepository.findByOwnerId(user.getId())).thenReturn(Collections.singletonList(item));
        when(itemRepository.save(any(Item.class))).thenReturn(item);

        ItemDto updatedItemDto = new ItemDto(item.getId(), "Обновленная Вещь", "Новое описание",
                true, null, null, user.getId(), null, null);
        ItemDto result = itemService.updateItem(user.getId(), item.getId(), updatedItemDto);

        assertNotNull(result);
        assertEquals(updatedItemDto.getName(), result.getName());
        verify(itemRepository).save(any(Item.class));
    }

    @Test
    void updateItem_ShouldThrowNotFoundException_WhenUserDoesNotExist() {
        when(userRepository.findById(user.getId())).thenReturn(Optional.empty());

        NotFoundException exception = assertThrows(NotFoundException.class, () -> {
            itemService.updateItem(user.getId(), item.getId(), itemDto);
        });

        assertEquals("Пользователь не найден", exception.getMessage());
        verify(itemRepository, never()).save(any(Item.class));
    }

    @Test
    void getItemById_ShouldReturnItemDto_WhenItemExists() {
        when(userRepository.findById(user.getId())).thenReturn(Optional.of(user));
        when(itemRepository.findById(item.getId())).thenReturn(Optional.of(item));

        ItemDto result = itemService.getItemById(user.getId(), item.getId());
        assertNotNull(result);
        assertEquals(item.getId(), result.getId());
        assertEquals(item.getName(), result.getName());
        verify(userRepository).findById(user.getId());
        verify(itemRepository).findById(item.getId());
    }

    @Test
    void getItemById_ShouldThrowNotFoundException_WhenItemDoesNotExist() {
        when(userRepository.findById(user.getId())).thenReturn(Optional.of(user));
        when(itemRepository.findById(item.getId())).thenReturn(Optional.empty());

        NotFoundException exception = assertThrows(NotFoundException.class, () -> {
            itemService.getItemById(user.getId(), item.getId());
        });

        assertEquals("Item not found", exception.getMessage());
        verify(userRepository).findById(user.getId());
        verify(itemRepository).findById(item.getId());
    }

    @Test
    void getAllItems_ShouldReturnListOfItemDto_WhenUserExists() {
        when(userRepository.findById(user.getId())).thenReturn(Optional.of(user));
        when(itemRepository.findByOwnerId(user.getId())).thenReturn(Collections.singletonList(item));

        List<ItemDto> result = itemService.getAllItems(user.getId());

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(item.getId(), result.get(0).getId());
        verify(userRepository).findById(user.getId());
        verify(itemRepository).findByOwnerId(user.getId());
    }

    @Test
    void getAllItems_ShouldThrowNotFoundException_WhenUserDoesNotExist() {
        when(userRepository.findById(user.getId())).thenReturn(Optional.empty());

        NotFoundException exception = assertThrows(NotFoundException.class, () -> {
            itemService.getAllItems(user.getId());
        });

        assertEquals("Пользователь не найден", exception.getMessage());
        verify(userRepository).findById(user.getId());
        verify(itemRepository, never()).findByOwnerId(anyLong());
    }

    @Test
    void search_ShouldReturnListOfItemDto_WhenTextIsValid() {
        String searchText = "Вещь";
        when(itemRepository.findAllByText(searchText)).thenReturn(Collections.singletonList(item));

        List<ItemDto> result = itemService.search(searchText);

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(item.getId(), result.get(0).getId());
        verify(itemRepository).findAllByText(searchText);
    }

    @Test
    void search_ShouldReturnEmptyList_WhenTextIsEmpty() {
        List<ItemDto> result = itemService.search("");

        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    @Test
    void comment_ShouldReturnCommentDto_WhenUserHasBooking() {
        NewCommentRequest newCommentRequest = new NewCommentRequest();
        newCommentRequest.setText("Отличная вещь!");
        Comment comment = new Comment();
        comment.setId(1L);
        comment.setText(newCommentRequest.getText());
        comment.setItem(item);
        comment.setAuthor(user);

        when(userRepository.findById(user.getId())).thenReturn(Optional.of(user));
        when(itemRepository.findById(item.getId())).thenReturn(Optional.of(item));
        Booking booking = new Booking(1L, LocalDateTime.now().minusDays(1), LocalDateTime.now().minusHours(1), item, user, null);
        when(bookingRepository.findByItemId(item.getId(), Sort.by(Sort.Direction.ASC, "start")))
                .thenReturn(Collections.singletonList(booking));
        when(commentRepository.save(any(Comment.class))).thenReturn(comment);

        CommentDto result = itemService.comment(user.getId(), item.getId(), newCommentRequest);

        assertNotNull(result);
        assertEquals(comment.getText(), result.getText());
        verify(userRepository).findById(user.getId());
        verify(itemRepository).findById(item.getId());
        verify(commentRepository).save(any(Comment.class));
    }

    @Test
    void comment_ShouldThrowValidationException_WhenUserHasBooking() {
        NewCommentRequest newCommentRequest = new NewCommentRequest();
        newCommentRequest.setText("Отличная вещь!");

        when(userRepository.findById(user.getId())).thenReturn(Optional.of(user));
        when(itemRepository.findById(item.getId())).thenReturn(Optional.of(item));


        ValidationException exception = assertThrows(ValidationException.class, () -> {
            itemService.comment(user.getId(), item.getId(), newCommentRequest);
        });

        assertEquals("Пользователь не может оставить комментарий", exception.getMessage());
        verify(userRepository).findById(user.getId());
        verify(itemRepository).findById(item.getId());
    }
}
