package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.booking.BookingRepository;
import ru.practicum.shareit.exception.InternalServerException;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.exception.ValidationException;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.UserRepository;
import ru.practicum.shareit.user.UserService;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class ItemServiceImpl implements ItemService {
    private final UserService userService;
    private final ItemRepository itemRepository;
    private final UserRepository userRepository;
    private final BookingRepository bookingRepository;
    private final CommentRepository commentRepository;
    private Long currentId = 1L;

    @Override
    public ItemDto addItem(long userId, ItemDto itemDto) {
        User user = userRepository.findById(userId).orElseThrow(() -> new NotFoundException("Пользователь не найден"));

        if (itemDto.getName() == null || itemDto.getName().isEmpty() ||
                itemDto.getDescription() == null || itemDto.getDescription().isEmpty()) {
            log.error("Имя или описание не указано");
            throw new InternalServerException("Имя или описание не указано");
        }
        Item item = ItemMapper.fromItemDto(itemDto);
        item.setId(currentId++);
        item.setOwner(user);
        item.setLastBooking(null);
        item.setNextBooking(null);

        log.info("Вещь с id {} добавлена", item.getId());
        return ItemMapper.toItemDto(itemRepository.save(item));

    }

    @Override
    public ItemDto updateItem(long userId, long itemId, ItemDto itemDto) {

        userRepository.findById(userId).orElseThrow(() -> new NotFoundException("Пользователь не найден"));

        List<Item> userItems = itemRepository.findByOwnerId(userId);
        Item item = userItems.stream().filter(item1 -> item1.getId() == itemId).findFirst()
                .orElseThrow(() -> new NotFoundException("Вещь не найдена"));
        if (itemDto.getName() != null) {
            item.setName(itemDto.getName());
        }
        if (itemDto.getDescription() != null) {
            item.setDescription(itemDto.getDescription());
        }
        if (itemDto.getAvailable() != null) {
                item.setAvailable(itemDto.getAvailable());
            }

            log.info("Вещь с id {} обновлена", item.getId());
            return ItemMapper.toItemDto(itemRepository.save(item));
        }

        @Override
        public ItemDto getItemById(long userId, long itemId) {
            List<Item> userItems = itemRepository.findByOwnerId(userId);
            if (userItems == null) {
                log.error("Вещь с id {} не найдена", itemId);
                throw new NotFoundException("Вещь не найдена");
            }

            Sort sort = Sort.by(Sort.Direction.ASC, "start");
            Item item = itemRepository.findById(itemId).orElseThrow(() -> new NotFoundException("Item not found"));
            if (!bookingRepository.findByBookerId(userId).isEmpty()) {
                bookingRepository.findByItemId(itemId, sort).stream()
                        .filter(booking -> booking.getItem().equals(item))
                        .findFirst()
                        .ifPresent(booking -> {
                                    if (booking.getStart().isAfter(LocalDateTime.now()) && item.getNextBooking() == null) {
                                        item.setNextBooking(booking.getStart());

                                    }
                                    if (booking.getEnd().isAfter(LocalDateTime.now())) {
                                        item.setLastBooking(booking.getEnd());

                                    }
                                }
                        );
            }

            List<Comment> comments = commentRepository.findByItemId(itemId);
            return ItemMapper.toItemDtoWithComments(item, comments);

        }

        @Override
        public List<ItemDto> getAllItems(long userId) {
            userRepository.findById(userId).orElseThrow(() -> new NotFoundException("Пользователь не найден"));

            return itemRepository.findByOwnerId(userId).stream()
                    .map(ItemMapper::toItemDto).toList();
        }

        @Override
        public List<ItemDto> search(String text) {
            if (text.isEmpty() || text.isBlank()) {
                return new ArrayList<>();
            }
            return itemRepository.findAllByText(text).stream()
                    .filter(Item::isAvailable)
                    .map(ItemMapper::toItemDto)
                    .toList();
        }

        @Override
        public CommentDto comment(long userId, long itemId, NewCommentRequest request) {
            User author = userRepository.findById(userId).orElseThrow(() -> new NotFoundException("Пользователь не найден"));
            Item item = itemRepository.findById(itemId).orElseThrow(() -> new NotFoundException("Вещь не найдена"));

            Sort sort = Sort.by(Sort.Direction.ASC, "start");
            boolean hasBooking = bookingRepository.findByItemId(itemId, sort).stream()
                    .anyMatch(booking -> userId == booking.getBooker().getId() && booking.getEnd().isBefore(LocalDateTime.now()));
            if (!hasBooking) {
                throw new ValidationException("Пользователь не может оставить комментарий");
            }
            Comment comment = CommentMapper.fromCommentRequest(request, author, item);

            return CommentMapper.toCommentDto(commentRepository.save(comment));
        }
    }