package ru.practicum.shareit.item.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.storage.BookingStorage;
import ru.practicum.shareit.exception.BadRequestException;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.item.coments.DTO.CommentsDTO;
import ru.practicum.shareit.item.coments.model.Comments;
import ru.practicum.shareit.item.coments.storage.CommentsStorage;
import ru.practicum.shareit.item.dto.ItemCreateRequestDto;
import ru.practicum.shareit.item.mapper.ItemMapper;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.storage.ItemStorage;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.request.storage.ItemRequestStorage;
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
    private final ItemRequestStorage  itemRequestStorage;


    public ItemService(@Autowired ItemStorage itemStorage,
                       @Autowired UserStorage userStorage,
                       @Autowired BookingStorage bookingStorage,
                       @Autowired CommentsStorage commentsStorage,
                       @Autowired ItemRequestStorage  itemRequestStorage
    ) {
        this.itemStorage = itemStorage;
        this.userStorage = userStorage;
        this.bookingStorage = bookingStorage;
        this.commentsStorage = commentsStorage;
        this.itemRequestStorage = itemRequestStorage;
    }

    public Item create(ItemCreateRequestDto item, Long userId) {
        User user = userStorage.findById(userId)
                .orElseThrow(() -> new NotFoundException("user not found by id: " + userId));
        ItemRequest itemRequest = item.getRequestId() != null ? itemRequestStorage.findById(item.getRequestId())
                .orElse(null) : null;

        return itemStorage.save(ItemMapper.toItem(null, item, user, itemRequest));
    }

    public Item update(ItemCreateRequestDto item, Long userId, Long itemId) {
        Item itemNew = itemStorage.findById(itemId)
                .orElseThrow(() -> new NotFoundException("item not found by id:" + itemId));

        if (!itemNew.getOwner().getId().equals(userId)) {
            throw new NotFoundException("not found");
        }
        User user = userStorage.findById(userId)
                .orElseThrow(() -> new NotFoundException("user not found by id: " + userId));
        ItemRequest itemRequest = item.getRequestId() != null ? itemRequestStorage.findById(item.getRequestId())
                .orElse(null) : null;

        return itemStorage.save(ItemMapper.toItem(itemNew, item, user, itemRequest));
    }

    public Item get(Long itemId, Long ownerId) {
        Item item = itemStorage.findById(itemId)
                .orElseThrow(() -> new NotFoundException("item not found by id:" + itemId));
        item.setComments(commentsStorage.findAllByItemId(itemId));
        item.setLastBooking(
                ItemMapper.toBookingToItem(
                        bookingStorage
                                .findFirstByEndTimeBeforeAndItemIdAndItem_OwnerIdOrderByEndTimeDesc(LocalDateTime.now(), itemId, ownerId).orElse(null)
                )
        );
        item.setNextBooking(
                ItemMapper.toBookingToItem(
                        bookingStorage
                                .findFirstByStartTimeAfterAndItemIdAndItem_OwnerIdAndStatusOrderByStartTime(LocalDateTime.now(), itemId, ownerId, APPROVED).orElse(null)
                )
        );

        return ItemMapper.toItem(item, ItemMapper.toItemResponseDto(item), item.getOwner(), item.getRequest());
    }

    public List<Item> getAll(Long userId, Integer from, Integer size) {
        if (from != null && size != null) {
            List<Item> items = itemStorage
                    .findAllByOwnerIdOrderById(userId, PageRequest.of(from / size, size));
            List<Item> newItem = new ArrayList<>();
            for (Item item : items) {
                item.setComments(commentsStorage.findAllByItemId(item.getId()));
                item.setLastBooking(
                        ItemMapper.toBookingToItem(
                                bookingStorage
                                        .findFirstByEndTimeBeforeAndItemIdAndItem_OwnerIdOrderByEndTimeDesc(LocalDateTime.now(), item.getId(), item.getOwner().getId()).orElse(null)
                        )
                );
                item.setNextBooking(
                        ItemMapper.toBookingToItem(
                                bookingStorage
                                        .findFirstByStartTimeAfterAndItemIdAndItem_OwnerIdAndStatusOrderByStartTime(LocalDateTime.now(), item.getId(), item.getOwner().getId(), APPROVED).orElse(null)
                        )
                );
                newItem.add(
                        ItemMapper.toItem(item, ItemMapper.toItemResponseDto(item), item.getOwner(), item.getRequest()));

            }
            return newItem;
        }
        List<Item> items = itemStorage
                .findAllByOwnerIdOrderById(userId);
        List<Item> newItem = new ArrayList<>();
        for (Item item : items) {
            item.setComments(commentsStorage.findAllByItemId(item.getId()));
            item.setLastBooking(
                    ItemMapper.toBookingToItem(
                            bookingStorage
                                    .findFirstByEndTimeBeforeAndItemIdAndItem_OwnerIdOrderByEndTimeDesc(LocalDateTime.now(), item.getId(), item.getOwner().getId()).orElse(null)
                    )
            );
            item.setNextBooking(
                    ItemMapper.toBookingToItem(
                            bookingStorage
                                    .findFirstByStartTimeAfterAndItemIdAndItem_OwnerIdAndStatusOrderByStartTime(LocalDateTime.now(), item.getId(), item.getOwner().getId(), APPROVED).orElse(null)
                    )
            );
            newItem.add(
                    ItemMapper.toItem(item, ItemMapper.toItemResponseDto(item), item.getOwner(), item.getRequest()));

        }
        return newItem;
    }

    public List<Item> getAllSearch(String search, Integer from, Integer size) {
        if (from != null && size != null) {
            Page<Item> items = itemStorage.findAll(PageRequest.of(from / size, size));
            List<Item> newItem = new ArrayList<>();
            for (Item item : items) {
                item.setComments(commentsStorage.findAllByItemId(item.getId()));
                item.setLastBooking(
                        ItemMapper.toBookingToItem(
                                bookingStorage
                                        .findFirstByEndTimeBeforeAndItemIdAndItem_OwnerIdOrderByEndTimeDesc(LocalDateTime.now(), item.getId(), item.getOwner().getId()).orElse(null)
                        )
                );
                item.setNextBooking(
                        ItemMapper.toBookingToItem(
                                bookingStorage
                                        .findFirstByStartTimeAfterAndItemIdAndItem_OwnerIdAndStatusOrderByStartTime(LocalDateTime.now(), item.getId(), item.getOwner().getId(), APPROVED).orElse(null)
                        )
                );
                newItem.add(
                        ItemMapper.toItem(item, ItemMapper.toItemResponseDto(item), item.getOwner(), item.getRequest()));

            }
            return newItem;
        }
        if (search == null || search.isEmpty()) {
            return new ArrayList<>();
        } else {
            List<Item> items = itemStorage
                    .findAllByNameContainingIgnoreCaseOrDescriptionContainingIgnoreCaseAndAvailable(search, search, true);
            List<Item> newItem = new ArrayList<>();
            for (Item item : items) {
                item.setComments(commentsStorage.findAllByItemId(item.getId()));
                item.setLastBooking(
                        ItemMapper.toBookingToItem(
                                bookingStorage
                                        .findFirstByEndTimeBeforeAndItemIdAndItem_OwnerIdOrderByEndTimeDesc(LocalDateTime.now(), item.getId(), item.getOwner().getId()).orElse(null)
                        )
                );
                item.setNextBooking(
                        ItemMapper.toBookingToItem(
                                bookingStorage
                                        .findFirstByStartTimeAfterAndItemIdAndItem_OwnerIdAndStatusOrderByStartTime(LocalDateTime.now(), item.getId(), item.getOwner().getId(), APPROVED).orElse(null)
                        )
                );
                newItem.add(
                        ItemMapper.toItem(item, ItemMapper.toItemResponseDto(item), item.getOwner(), item.getRequest()));

            }
            return newItem;
        }

    }

    public Comments addComment(Long userId, Long itemId, CommentsDTO commentsDTO) {
        Item item = itemStorage.findById(itemId)
                .orElseThrow(() -> new BadRequestException("item not found by id:" + itemId));
        User user = userStorage.findById(userId)
                .orElseThrow(() -> new NotFoundException("user not found by id:" + userId));
        Booking  booking = bookingStorage.findFirstByBookerIdAndItemIdAndStatusAndEndTimeBefore(userId, itemId, APPROVED, LocalDateTime.now())
                .orElseThrow(() -> new BadRequestException("вы не можете остовлять коментарий!"));
        Comments comments = commentsStorage.save(new Comments(null, commentsDTO.getText(), itemId, user, LocalDateTime.now()));
        return comments;


    }


}
