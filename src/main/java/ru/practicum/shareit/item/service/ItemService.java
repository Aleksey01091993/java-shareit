package ru.practicum.shareit.item.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exception.NotFound404;
import ru.practicum.shareit.item.dto.ItemDTO;
import ru.practicum.shareit.item.mapper.ItemMapper;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.storage.ItemStorage;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.storage.UserStorage;

import java.util.ArrayList;
import java.util.List;
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

    public Item create(ItemDTO item, Long userId) {
        User user = userStorage.findById(userId)
                .orElseThrow(() -> new NotFound404("user not found by id: " + userId));

        Item itemOne = itemStorage.save(ItemMapper.toItem(item, user));
        return ItemMapper.toItemDto(itemOne);
    }

    public Item update(ItemDTO item, Long userId, Long itemId) {
        Item itemNew = itemStorage.findById(itemId)
                .orElseThrow(() -> new NotFound404("item not found by id:" + itemId));

        if (!itemNew.getOwner().getId().equals(userId)) {
            throw new NotFound404("not found");
        }
        userStorage.findById(userId)
                .orElseThrow(() -> new NotFound404("user not found by id: " + userId));

        if (item.getName() != null) {
            itemNew.setName(item.getName());
        }
        if (item.getAvailable() != null) {
            itemNew.setAvailable(item.getAvailable());
        }
        if (item.getDescription() != null) {
            itemNew.setDescription(item.getDescription());
        }
        return itemStorage.save(itemNew);
    }

    public Item get(Long itemId) {
        return itemStorage.findById(itemId)
                .orElseThrow(() -> new NotFound404("item not found by id:" + itemId));
    }

    public List<Item> getAll(Long userId) {
        return itemStorage.findAll().stream()
                .filter(o1 -> o1.getOwner() != null)
                .filter(o1 -> (o1.getOwner().getId().equals(userId)))
                .collect(Collectors.toList());
    }

    public List<Item> getAllSearch(String search) {
        if (search == null || search.isEmpty()) {
            return new ArrayList<>();
        } else {
            return itemStorage.findAll().stream()
                    .filter(o1 -> o1.getName().toLowerCase().contains(search.toLowerCase()) ||
                            o1.getDescription().toLowerCase().contains(search.toLowerCase()))
                    .filter(Item::getAvailable)
                    .collect(Collectors.toList());
        }

    }
}
