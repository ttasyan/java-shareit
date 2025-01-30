package ru.practicum.shareit.item.model;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * TODO Sprint add-controllers.
 */
@Data
@AllArgsConstructor
public class Item {
    private long id;
    private String name;
    private String description;
    private boolean available;

}
