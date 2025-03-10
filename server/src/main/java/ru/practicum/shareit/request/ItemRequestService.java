package ru.practicum.shareit.request;

import ru.practicum.shareit.request.dto.ItemRequestDto;

import java.util.List;

public interface ItemRequestService {
    ItemRequestDto addRequest(long userId, NewItemRequest request);

    List<ItemRequestDto> get(long userId);

    List<ItemRequestDto> getAll(long userId);

    ItemRequestDto findById(long requestId);

}
