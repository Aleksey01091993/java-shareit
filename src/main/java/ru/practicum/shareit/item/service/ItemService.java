package ru.practicum.shareit.item.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.storage.BookingStorage;
import ru.practicum.shareit.exception.BadRequest400;
import ru.practicum.shareit.exception.NotFound404;
import ru.practicum.shareit.item.coments.DTO.CommentsDTO;
import ru.practicum.shareit.item.coments.model.Comments;
import ru.practicum.shareit.item.coments.storage.CommentsStorage;
import ru.practicum.shareit.item.dto.ItemResponseDTO;
import ru.practicum.shareit.item.mapper.ItemMapper;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.storage.ItemStorage;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.storage.UserStorage;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static ru.practicum.shareit.booking.status.Status.APPROVED;

@Service
public class ItemService implements Serializable {

    private final ItemStorage itemStorage;
    private final UserStorage userStorage;
    private final BookingStorage bookingStorage;

    private final CommentsStorage commentsStorage;


    public ItemService(@Autowired ItemStorage itemStorage,
                       @Autowired UserStorage userStorage,
                       @Autowired BookingStorage bookingStorage,
                       @Autowired CommentsStorage commentsStorage
    ) {
        this.itemStorage = itemStorage;
        this.userStorage = userStorage;
        this.bookingStorage = bookingStorage;
        this.commentsStorage = commentsStorage;
    }

    public Item create(ItemResponseDTO item, Long userId) {
        User user = userStorage.findById(userId)
                .orElseThrow(() -> new NotFound404("user not found by id: " + userId));

        Item itemOne = itemStorage.save(ItemMapper.toItem(null, item, null, null, user, null));
        return itemOne;
    }

    public Item update(ItemResponseDTO item, Long userId, Long itemId) {
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
        Item itemNew = ItemMapper.toItem(
                itemId,
                ItemMapper.toItemResponseDto(item),
                bookingStorage
                        .findFirstByEndTimeBeforeAndItemIdAndItem_OwnerIdOrderByEndTimeDesc(LocalDateTime.now(), itemId, ownerId).orElse(null),
                bookingStorage
                        .findFirstByStartTimeAfterAndItemIdAndItem_OwnerIdAndStatusOrderByStartTime(LocalDateTime.now(), itemId, ownerId, APPROVED).orElse(null),
                item.getOwner(),
                commentsStorage.
                        findAllByItemId(itemId)
                );
        return itemNew;

    }

    public List<Item> getAll(Long userId) {
        List<Item> items = itemStorage
                .findAllByOwnerId(userId);
        List<Item> newItem = new ArrayList<>();
        for (Item item : items) {
            newItem.add(
                    ItemMapper.toItem(
                            item.getId(),
                            ItemMapper.toItemResponseDto(item),
                            bookingStorage
                                    .findFirstByEndTimeBeforeAndItemIdAndItem_OwnerIdOrderByEndTimeDesc(LocalDateTime.now(), item.getId(), userId).orElse(null),
                            bookingStorage
                                    .findFirstByStartTimeAfterAndItemIdAndItem_OwnerIdAndStatusOrderByStartTime(LocalDateTime.now(), item.getId(), userId, APPROVED).orElse(null),
                            item.getOwner(),
                            commentsStorage.findAllByItemId(item.getId())));

        }
        return newItem;
    }

    public List<Item> getAllSearch(String search) {
        if (search == null || search.isEmpty()) {
            return new ArrayList<>();
        } else {
            List<Item> items = itemStorage
                    .findAllByNameContainingIgnoreCaseOrDescriptionContainingIgnoreCaseAndAvailable(search, search, true);
            List<Item> newItem = new ArrayList<>();
            for (Item item : items) {
                newItem.add(
                        ItemMapper.toItem(
                                item.getId(),
                                ItemMapper.toItemResponseDto(item),
                                bookingStorage
                                        .findFirstByEndTimeBeforeAndItemIdAndItem_OwnerIdOrderByEndTimeDesc(LocalDateTime.now(), item.getId(), item.getOwner().getId()).orElse(null),
                                bookingStorage
                                        .findFirstByStartTimeAfterAndItemIdAndItem_OwnerIdAndStatusOrderByStartTime(LocalDateTime.now(), item.getId(), item.getOwner().getId(), APPROVED).orElse(null),
                                item.getOwner(),
                                commentsStorage.findAllByItemId(item.getId())));

            }
            return newItem;
        }

    }

    public Comments addComment(Long userId, Long itemId, CommentsDTO commentsDTO) {
        Item item = itemStorage.findById(itemId)
                .orElseThrow(() -> new BadRequest400("item not found by id:" + itemId));
        User user = userStorage.findById(userId)
                .orElseThrow(() -> new NotFound404("user not found by id:" + userId));
        Booking  booking = bookingStorage.findFirstByBookerIdAndItemIdAndStatusAndEndTimeBefore(userId, itemId, APPROVED, LocalDateTime.now())
                .orElseThrow(() -> new BadRequest400("вы не можете остовлять коментарий!"));
        Comments comments = commentsStorage.save(new Comments(null, commentsDTO.getText(), itemId, user, LocalDateTime.now()));
        return comments;


    }


}
