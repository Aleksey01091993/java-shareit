package ru.practicum.shareit.item.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exception.BadRequest400;
import ru.practicum.shareit.exception.NotFound404;
import ru.practicum.shareit.item.dto.ItemDTO;
import ru.practicum.shareit.item.mapper.ItemMapper;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.storage.ItemStorage;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.storage.UserStorage;

import java.util.NoSuchElementException;

@Service
public class ItemService {

    private final ItemStorage itemStorage;
    private final UserStorage userStorage;


    public ItemService(@Autowired ItemStorage itemStorage,
                       @Autowired UserStorage userStorage) {
        this.itemStorage = itemStorage;
        this.userStorage = userStorage;
    }

    public ItemDTO create(Item item, Long userId) throws Exception {
        if (item.getAvailable() == null || item.getDescription() == null || item.getName() == null
                || item.getDescription().isEmpty() || item.getName().isEmpty()) {
            throw new BadRequest400("поле не может быть пустым");
        }
        User user;
        try {
            user = userStorage.findById(userId).get();
        } catch (NoSuchElementException e) {
            throw new NotFound404("user not found");
        }
        item.setOwner(user);
        return ItemMapper.toItemDto(itemStorage.save(item));
    }

    public ItemDTO update(Item item, Long userId, Long itemId) throws Exception {
        Item itemNew = itemStorage.findById(itemId).get();
        try {
            if (!itemNew.getOwner().getId().equals(userId)) {
                throw new NotFound404("not found");
            }
            userStorage.findById(userId).get();
        } catch (NoSuchElementException e) {
            throw new NotFound404("user not found");
        }
        if (item.getName() != null) {
            itemNew.setName(item.getName());
        }
        if (item.getAvailable() != null) {
            itemNew.setAvailable(item.getAvailable());
        }
        if (item.getDescription() != null) {
            itemNew.setDescription(item.getDescription());
        }
        ItemDTO itemDTO = ItemMapper.toItemDto(itemStorage.save(itemNew));
        return itemDTO;
    }


}
