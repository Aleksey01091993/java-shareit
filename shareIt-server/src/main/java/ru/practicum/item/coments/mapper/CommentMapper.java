package ru.practicum.item.coments.mapper;

import ru.practicum.item.coments.DTO.CommentsDTO;
import ru.practicum.item.coments.model.Comments;

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
