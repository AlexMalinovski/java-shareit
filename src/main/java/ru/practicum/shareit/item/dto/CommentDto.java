package ru.practicum.shareit.item.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CommentDto {
    private final Long id;
    private final String text;
    private final String authorName;
    private final String created;
}
