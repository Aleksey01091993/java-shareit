package ru.practicum.shareit.booking.service;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.booking.dto.BookingCreationDTO;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.storage.BookingStorage;
import ru.practicum.shareit.exception.BadRequest400;
import ru.practicum.shareit.exception.InternalServerError500;
import ru.practicum.shareit.exception.NotFound404;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.storage.ItemStorage;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.storage.UserStorage;

import java.time.LocalDateTime;
import java.util.List;

import static ru.practicum.shareit.booking.status.Status.*;

@Service
public class BookingService {
    private final UserStorage userStorage;
    private final ItemStorage itemStorage;
    private final BookingStorage bookingStorage;

    public BookingService(
            @Autowired UserStorage userStorage,
            @Autowired ItemStorage itemStorage,
            @Autowired BookingStorage bookingStorage
    ) {
        this.userStorage = userStorage;
        this.itemStorage = itemStorage;
        this.bookingStorage = bookingStorage;
    }

    public Booking create(BookingCreationDTO bookingCreationDTO, Long userId) {
        Item item = itemStorage.findById(bookingCreationDTO.getItemId())
                .orElseThrow(() -> new NotFound404("item not found by id: " + bookingCreationDTO.getItemId()));
        if (bookingCreationDTO.getStart().isAfter(bookingCreationDTO.getEnd()) ||
                bookingCreationDTO.getStart().equals(bookingCreationDTO.getEnd())) {
            throw new BadRequest400("дата окончания позже даты начала");
        }
        if (!item.getAvailable()) {
            throw new BadRequest400("вещь недоступна для бронирования");
        }

        User user = userStorage.findById(userId)
                .orElseThrow(() -> new NotFound404("user not found by id:" + userId));
        if (userId.equals(item.getOwner().getId())) {
            throw new NotFound404("Владелец вещи не может ее забронировать");
        }

        Booking booking = new Booking(
                bookingCreationDTO.getId(),
                bookingCreationDTO.getStart(),
                bookingCreationDTO.getEnd(),
                item,
                user,
                WAITING
        );
        return bookingStorage.save(booking);


    }

    public Booking approved(Long bookingId, Long userId, Boolean approved) {
        Booking newBooking = bookingStorage.findById(bookingId)
                .orElseThrow(() -> new NotFound404("booking not found by id: " + bookingId));
        userStorage.findById(userId)
                .orElseThrow(() -> new NotFound404("owner not found by id:" + userId));
        if (newBooking.getItem().getOwner().getId().equals(userId)) {
            if (newBooking.getStatus() != WAITING) {
                throw new BadRequest400("status error");
            }
            if (approved) {
                newBooking.setStatus(APPROVED);
            } else {
                newBooking.setStatus(REJECTED);
            }
            return bookingStorage.save(newBooking);
        } else {
            throw new NotFound404("owner not found by id:" + userId);
        }
    }

    public Booking get(Long bookingId, Long userOrOwnerId) {
        Booking newBooking = bookingStorage.findById(bookingId)
                .orElseThrow(() -> new NotFound404("booking not found by id: " + bookingId));
        if (newBooking.getItem().getOwner().getId().equals(userOrOwnerId) ||
                newBooking.getBooker().getId().equals(userOrOwnerId)) {
            return newBooking;
        } else {
            throw new NotFound404("user or owner not found by id: " + userOrOwnerId);
        }
    }

    public List<Booking> getAll(Long bookerId, String state) {
        if (state == null || state.equals("ALL")) {
            return bookingStorage
                    .findByBooker_IdOrderByStartTimeDesc(bookerId);
        } else if (state.equals("CURRENT")) {
            return bookingStorage
                    .findByBooker_IdAndStartTimeLessThanAndEndTimeGreaterThanOrderByStartTimeDesc(bookerId, LocalDateTime.now(), LocalDateTime.now());
        } else if (state.equals("PAST")) {
            return bookingStorage.findByBooker_IdAndEndTimeGreaterThanOrderByStartTimeDesc(bookerId, LocalDateTime.now());
        } else if (state.equals("FUTURE")) {
            return bookingStorage
                    .findByBooker_IdAndStartTimeGreaterThanOrderByStartTimeDesc(bookerId, LocalDateTime.now());
        } else if (state.equals("WAITING")) {
            return bookingStorage
                    .findByBooker_IdAndStatusOrderByStartTimeDesc(bookerId, WAITING);
        } else if (state.equals("REJECTED")) {
            return bookingStorage
                    .findByBooker_IdAndStatusOrderByStartTimeDesc(bookerId, REJECTED);
        } else {
            throw new InternalServerError500("Unknown state: " + state);
        }

    }

    public List<Booking> getAllOwnerId(Long ownerId, String state) {
        userStorage.findById(ownerId)
                .orElseThrow(() -> new InternalServerError500("owner not found by id:" + ownerId));
        if (state == null || state.equals("ALL")) {
            return bookingStorage.findByItem_OwnerIdOrderByStartTimeDesc(ownerId);
        } else if (state.equals("CURRENT")) {
            return bookingStorage
                    .findByItem_OwnerIdAndStartTimeLessThanAndEndTimeGreaterThanOrderByStartTimeDesc(ownerId, LocalDateTime.now(), LocalDateTime.now());
        } else if (state.equals("PAST")) {
            return bookingStorage
                    .findByItem_OwnerIdAndEndTimeGreaterThanOrderByStartTimeDesc(ownerId, LocalDateTime.now());
        } else if (state.equals("FUTURE")) {
            return bookingStorage
                    .findByItem_OwnerIdAndStartTimeGreaterThanOrderByStartTimeDesc(ownerId, LocalDateTime.now());
        } else if (state.equals("WAITING")) {
            return bookingStorage
                    .findByItem_OwnerIdAndStatusOrderByStartTimeDesc(ownerId, WAITING);
        } else if (state.equals("REJECTED")) {
            return bookingStorage
                    .findByItem_OwnerIdAndStatusOrderByStartTimeDesc(ownerId, REJECTED);
        } else {
            throw new InternalServerError500("Unknown state: " + state);
        }
    }


}
