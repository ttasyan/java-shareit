package ru.practicum.shareit.item;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class NewCommentRequest {
    private String text;
}
