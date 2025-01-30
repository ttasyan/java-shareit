package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exception.InternalServerException;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.UserService;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@Slf4j
@RequiredArgsConstructor
public class ItemServiceImpl implements ItemService {
    private final UserService userService;
    private static Long currentId = 1L;
    private final Map<Long, List<Item>> items = new HashMap<>();

    public ItemDto addItem(long userId, ItemDto itemDto) {
        userService.getUserById(userId);
        if (itemDto.getName() == null || itemDto.getName().isEmpty() ||
                itemDto.getDescription() == null || itemDto.getDescription().isEmpty()) {
            log.error("Имя или описание не указано");
            throw new InternalServerException("Имя или описание не указано");
        }
        Item item = ItemMapper.fromItemDto(itemDto);
        item.setId(currentId++);
        List<Item> userItems = items.getOrDefault(userId, new ArrayList<>());
        userItems.add(item);
        items.put(userId, userItems);
        log.info("Вещь с id {} добавлена", item.getId());
        return ItemMapper.toItemDto(item);

    }

    public ItemDto updateItem(long userId, long itemId, ItemDto itemDto) {
        userService.getUserById(userId);
        if (items.get(userId).isEmpty()) {
            log.error("Id юзера отстуствует");
            throw new InternalServerException("Id юзера отстуствует");
        }
        List<Item> userItems = items.get(userId);
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

        items.put(userId, userItems);
        log.info("Вещь с id {} обновлена", item.getId());
        return ItemMapper.toItemDto(item);
    }

    public ItemDto getItemById(long userId, long itemId) {
        List<Item> userItems = items.get(userId);
        if (userItems == null) {
            log.error("Вещь с id {} не найдена", itemId);
            throw new NotFoundException("Вещь не найдена");
        }
        return items.get(userId).stream().filter(item -> item.getId() == itemId)
                .findFirst().map(ItemMapper::toItemDto)
                .orElseThrow(() -> new NotFoundException("Вещь не найдена"));

    }

    public List<ItemDto> getAllItems(long userId) {
        return items.getOrDefault(userId, new ArrayList<>()).stream()
                .map(ItemMapper::toItemDto).toList();
    }

    public List<ItemDto> search(String text) {
        if (text.isEmpty() || text.isBlank()) {
            return new ArrayList<>();
        }
        return items.values().stream()
                .flatMap(List::stream)
                .filter(Item::isAvailable)
                .filter(item -> item.getName().toLowerCase().contains(text.toLowerCase()) ||
                        item.getDescription().toLowerCase().contains(text.toLowerCase()))
                .map(ItemMapper::toItemDto)
                .toList();
    }
}
