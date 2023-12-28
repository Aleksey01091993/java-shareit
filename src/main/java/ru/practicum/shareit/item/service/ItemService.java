package ru.practicum.shareit.item.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.booking.storage.BookingStorage;
import ru.practicum.shareit.exception.NotFound404;
import ru.practicum.shareit.item.dto.ItemDTO;
import ru.practicum.shareit.item.mapper.ItemMapper;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.storage.ItemStorage;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.storage.UserStorage;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class ItemService implements Serializable {

    private final ItemStorage itemStorage;
    private final UserStorage userStorage;
    private final BookingStorage bookingStorage;


    public ItemService(@Autowired ItemStorage itemStorage,
                       @Autowired UserStorage userStorage,
                       @Autowired BookingStorage bookingStorage) {
        this.itemStorage = itemStorage;
        this.userStorage = userStorage;
        this.bookingStorage = bookingStorage;
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

    public Item get(Long itemId, Long ownerId) {
        Item item = itemStorage.findById(itemId)
                .orElseThrow(() -> new NotFound404("item not found by id:" + itemId));
        return ItemMapper.toItemBookerDTO(item,
                bookingStorage
                        .findFirstByEndTimeBeforeAndItemIdAndItem_OwnerIdOrderByEndTime(LocalDateTime.now(), itemId, ownerId).orElse(null),
                bookingStorage
                        .findFirstByStartTimeAfterAndItemIdAndItem_OwnerIdOrderByStartTime(LocalDateTime.now(), itemId, ownerId).orElse(null));

    }

    public List<Item> getAll(Long userId) {
        List<Item> items = itemStorage
                .findAllByOwnerId(userId);
        List<Item> newItem = new ArrayList<>();
        for (Item item : items) {
            newItem.add(
                    ItemMapper.toItemBookerDTO(item,
                            bookingStorage
                                    .findFirstByEndTimeBeforeAndItemIdAndItem_OwnerIdOrderByEndTime(LocalDateTime.now(), item.getId(), userId).orElse(null),
                            bookingStorage
                                    .findFirstByStartTimeAfterAndItemIdAndItem_OwnerIdOrderByStartTime(LocalDateTime.now(), item.getId(), userId).orElse(null)));

        }
        return newItem;
    }

    public List<Item> getAllSearch(String search) {
        if (search == null || search.isEmpty()) {
            return new ArrayList<>();
        } else {
            return itemStorage
                    .findAllByNameContainingIgnoreCaseOrDescriptionContainingIgnoreCaseAndAvailable(search, search, true);
        }

    }
}
