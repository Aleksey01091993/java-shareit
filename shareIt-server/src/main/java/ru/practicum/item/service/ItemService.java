package ru.practicum.item.service;


import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Controller;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.*;
import ru.practicum.booking.storage.BookingStorage;
import ru.practicum.exception.BadRequestException;
import ru.practicum.exception.NotFoundException;
import ru.practicum.item.coments.DTO.CommentsDTO;
import ru.practicum.item.coments.mapper.CommentMapper;
import ru.practicum.item.coments.model.Comments;
import ru.practicum.item.coments.storage.CommentsStorage;
import ru.practicum.item.dto.ItemCreateRequestDto;
import ru.practicum.item.dto.ItemResponseDto;
import ru.practicum.item.mapper.ItemMapper;
import ru.practicum.item.model.Item;
import ru.practicum.item.storage.ItemStorage;
import ru.practicum.request.model.ItemRequest;
import ru.practicum.request.storage.ItemRequestStorage;
import ru.practicum.user.model.User;
import ru.practicum.user.storage.UserStorage;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static ru.practicum.booking.status.Status.APPROVED;

@Service
@Controller
@RequestMapping(path = "/items")
@RequiredArgsConstructor
public class ItemService implements Serializable {

    private final ItemStorage itemStorage;
    private final UserStorage userStorage;
    private final BookingStorage bookingStorage;
    private final CommentsStorage commentsStorage;
    private final ItemRequestStorage  itemRequestStorage;


    @PostMapping
    public ResponseEntity<Object> create(
            @RequestBody ItemCreateRequestDto item,
            @RequestHeader("X-Sharer-User-Id") Long userId
    ) {
        User user = userStorage.findById(userId)
                .orElseThrow(() -> new NotFoundException("user not found by id: " + userId));
        ItemRequest itemRequest = item.getRequestId() != null ? itemRequestStorage.findById(item.getRequestId())
                .orElse(null) : null;
        Item newItem = itemStorage.save(ItemMapper.toItem(null, item, user, itemRequest));
        ItemResponseDto itemDTO = ItemMapper.toItemDTO(newItem, newItem.getComments());
        return new ResponseEntity<>(itemDTO, HttpStatus.OK);

    }
    @PatchMapping("/{itemId}")
    public ResponseEntity<Object> update(
            @RequestBody @Nullable ItemCreateRequestDto item,
            @RequestHeader("X-Sharer-User-Id") Long userId,
            @PathVariable Long itemId
    ) {
        Item itemNew = itemStorage.findById(itemId)
                .orElseThrow(() -> new NotFoundException("item not found by id:" + itemId));

        if (!itemNew.getOwner().getId().equals(userId)) {
            throw new NotFoundException("not found");
        }
        User user = userStorage.findById(userId)
                .orElseThrow(() -> new NotFoundException("user not found by id: " + userId));
        ItemRequest itemRequest = item.getRequestId() != null ? itemRequestStorage.findById(item.getRequestId())
                .orElse(null) : null;
        Item newItem = itemStorage.save(ItemMapper.toItem(itemNew, item, user, itemRequest));
        return new ResponseEntity<>(ItemMapper.toItemDTO(newItem, newItem.getComments()), HttpStatus.OK);
    }
    @GetMapping("/{itemId}")
    public ResponseEntity<Object> get(
            @PathVariable Long itemId,
            @RequestHeader("X-Sharer-User-Id") Long userId
    ) {
        Item item = itemStorage.findById(itemId)
                .orElseThrow(() -> new NotFoundException("item not found by id:" + itemId));
        item.setComments(commentsStorage.findAllByItemId(itemId));
        item.setLastBooking(
                ItemMapper.toBookingToItem(
                        bookingStorage
                                .findFirstByEndTimeBeforeAndItemIdAndItem_OwnerIdOrderByEndTimeDesc(LocalDateTime.now(), itemId, item.getOwner().getId()).orElse(null)
                )
        );
        item.setNextBooking(
                ItemMapper.toBookingToItem(
                        bookingStorage
                                .findFirstByStartTimeAfterAndItemIdAndItem_OwnerIdAndStatusOrderByStartTime(LocalDateTime.now(), itemId, item.getOwner().getId(), APPROVED).orElse(null)
                )
        );
        Item newItem = ItemMapper.toItem(item, ItemMapper.toItemResponseDto(item), item.getOwner(), item.getRequest());
        return new ResponseEntity<>(ItemMapper.toItemDTO(newItem, newItem.getComments()), HttpStatus.OK);
    }
    @GetMapping
    public ResponseEntity<Object> getAll(
            @RequestHeader("X-Sharer-User-Id") Long userId,
            @RequestParam @Nullable Integer from,
            @RequestParam @Nullable Integer size
    ) {
        if (from != null && size != null) {
            List<Item> items = itemStorage
                    .findAllByOwnerIdOrderById(userId, PageRequest.of(from / size, size));
            List<ItemResponseDto> newItem = new ArrayList<>();
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
                Item addItem = ItemMapper.toItem(item, ItemMapper.toItemResponseDto(item), item.getOwner(), item.getRequest());
                newItem.add(
                        ItemMapper.toItemDTO(addItem, addItem.getComments())
                );


            }

            return new ResponseEntity<>(newItem, HttpStatus.OK);
        }
        List<Item> items = itemStorage
                .findAllByOwnerIdOrderById(userId);
        List<ItemResponseDto> newItem = new ArrayList<>();
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
            Item addItem = ItemMapper.toItem(item, ItemMapper.toItemResponseDto(item), item.getOwner(), item.getRequest());
            newItem.add(
                    ItemMapper.toItemDTO(addItem, addItem.getComments())
            );

        }
        return new ResponseEntity<>(newItem, HttpStatus.OK);
    }
    @GetMapping("/search")
    public ResponseEntity<Object> getAllSearch(
            @RequestParam @Nullable String text,
            @RequestParam @Nullable Integer from,
            @RequestParam @Nullable Integer size
    ) {
        if (from != null && size != null) {
            Page<Item> items = itemStorage.findAll(PageRequest.of(from / size, size));
            List<ItemResponseDto> newItem = new ArrayList<>();
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
                Item addItem = ItemMapper.toItem(item, ItemMapper.toItemResponseDto(item), item.getOwner(), item.getRequest());
                        newItem.add(
                        ItemMapper.toItemDTO(addItem, addItem.getComments())
                                );

            }
            return new ResponseEntity<>(newItem, HttpStatus.OK);
        }
        if (text == null || text.isEmpty()) {
            return new ResponseEntity<>(Collections.emptyList(), HttpStatus.OK);
        } else {
            List<Item> items = itemStorage
                    .findAllByNameContainingIgnoreCaseOrDescriptionContainingIgnoreCaseAndAvailable(text, text, true);
            List<ItemResponseDto> newItem = new ArrayList<>();
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
                Item addItem = ItemMapper.toItem(item, ItemMapper.toItemResponseDto(item), item.getOwner(), item.getRequest());
                        newItem.add(
                                ItemMapper.toItemDTO(addItem, addItem.getComments())
                        );

            }
            return new ResponseEntity<>(newItem, HttpStatus.OK);
        }

    }
    @PostMapping("/{itemId}/comment")
    public ResponseEntity<Object> addComment(
            @RequestHeader("X-Sharer-User-Id") Long userId,
            @PathVariable Long itemId,
            @RequestBody CommentsDTO commentsDTO
    ) {
        itemStorage.findById(itemId)
                .orElseThrow(() -> new BadRequestException("item not found by id:" + itemId));
        User user = userStorage.findById(userId)
                .orElseThrow(() -> new NotFoundException("user not found by id:" + userId));
        bookingStorage.findFirstByBookerIdAndItemIdAndStatusAndEndTimeBefore(userId, itemId, APPROVED, LocalDateTime.now())
                .orElseThrow(() -> new BadRequestException("вы не можете остовлять коментарий!"));
        Comments comments = commentsStorage.save(new Comments(null, commentsDTO.getText(), itemId, user, LocalDateTime.now()));
        return new ResponseEntity<>(CommentMapper.toCommentsDTO(comments), HttpStatus.OK);
    }


}
