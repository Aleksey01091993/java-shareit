package ru.practicum.shareit.item.storage.dal;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import ru.practicum.shareit.item.model.Item;

@Repository
public interface ItemStorage extends CrudRepository<Item, Long> {
}
