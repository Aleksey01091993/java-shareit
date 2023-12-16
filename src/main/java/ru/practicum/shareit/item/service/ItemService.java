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

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

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
        Item itemOne = itemStorage.save(item);
        return ItemMapper.toItemDto(itemOne);
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

    public ItemDTO get(Long itemId) throws Exception {
        Item item = itemStorage.findById(itemId).get();
        return ItemMapper.toItemDto(item);
    }

    public List<ItemDTO> getAll(Long userId) {
        return itemStorage.findAll().stream()
                .filter(o1 -> o1.getOwner() != null)
                .filter(o1 -> (o1.getOwner().getId().equals(userId)))
                .map(ItemMapper::toItemDto)
                .collect(Collectors.toList());
    }

    public List<ItemDTO> getAllSearch(String search) {
        if (search.isEmpty()) {
            return new ArrayList<>();
        } else {
            return itemStorage.findAll().stream()
                    .filter(o1 -> o1.getName().toLowerCase().contains(search.toLowerCase()) ||
                            o1.getDescription().toLowerCase().contains(search.toLowerCase()))
                    .filter(Item::getAvailable)
                    .map(ItemMapper::toItemDto)
                    .collect(Collectors.toList());
        }

    }
}
