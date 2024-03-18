package ru.practicum.item.coments.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class CommentsDTO {
    private Long id;
    private String text;
    private String authorName;
    private LocalDateTime created;
}
