package ru.practicum.shareit.booking.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.User;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BookingDto {
    private long id;
    private String start;
    private String end;
    private Item item;
    private User booker;
    private String status;
}
