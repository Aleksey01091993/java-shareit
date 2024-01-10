package ru.practicum.shareit.item.coments.mapper;

import ru.practicum.shareit.item.coments.DTO.CommentsDTO;
import ru.practicum.shareit.item.coments.model.Comments;

public class CommentMapper {
    public static CommentsDTO toCommentsDTO(Comments comment) {
        return new CommentsDTO(
                comment.getId(),
                comment.getText(),
                comment.getAuthor().getName(),
                comment.getCreated()
        );
    }
}
