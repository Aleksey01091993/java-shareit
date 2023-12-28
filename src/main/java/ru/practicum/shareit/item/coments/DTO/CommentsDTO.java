package ru.practicum.shareit.item.coments.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import ru.practicum.shareit.item.coments.model.Comments;

import java.time.LocalDateTime;

@Data
@EqualsAndHashCode(callSuper = true)
@AllArgsConstructor
public class CommentsDTO extends Comments {
    private Long id;
    private String text;
    private String authorName;
    private LocalDateTime created;
}
