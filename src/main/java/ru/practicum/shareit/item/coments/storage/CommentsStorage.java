package ru.practicum.shareit.item.coments.storage;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.practicum.shareit.item.coments.model.Comments;

import java.util.List;

@Repository
public interface CommentsStorage extends JpaRepository<Comments, Long> {
    List<Comments> findAllByItemId(Long itemId);
}
