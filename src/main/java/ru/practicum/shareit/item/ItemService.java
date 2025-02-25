package ru.practicum.shareit.item;

import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;

import java.util.List;

public interface ItemService {
    ItemDto addItem(long userId, ItemDto itemDto);

    ItemDto updateItem(long userId, long itemId, ItemDto itemDto);

    ItemDto getItemById(long userId, long itemID);

    List<ItemDto> getAllItems(long userId);

    List<ItemDto> search(String text);

    CommentDto comment(long userId, long itemId, NewCommentRequest request);
}
