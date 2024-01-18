package ru.practicum.shareit.booking.service;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.booking.dto.BookingRequestDto;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.storage.BookingStorage;
import ru.practicum.shareit.exception.BadRequestException;
import ru.practicum.shareit.exception.InternalServerErrorException;
import ru.practicum.shareit.exception.NotFoundException;
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

    public Booking create(BookingRequestDto bookingRequestDto, Long userId) {
        Item item = itemStorage.findById(bookingRequestDto.getItemId())
                .orElseThrow(() -> new NotFoundException("item not found by id: " + bookingRequestDto.getItemId()));
        if (bookingRequestDto.getStart().isAfter(bookingRequestDto.getEnd()) ||
                bookingRequestDto.getStart().equals(bookingRequestDto.getEnd())) {
            throw new BadRequestException("дата окончания позже даты начала");
        }
        if (!item.getAvailable()) {
            throw new BadRequestException("вещь недоступна для бронирования");
        }

        User user = userStorage.findById(userId)
                .orElseThrow(() -> new NotFoundException("user not found by id:" + userId));
        if (userId.equals(item.getOwner().getId())) {
            throw new NotFoundException("Владелец вещи не может ее забронировать");
        }

        Booking booking = new Booking(
                null,
                bookingRequestDto.getStart(),
                bookingRequestDto.getEnd(),
                item,
                user,
                WAITING
        );
        Booking newBooking = bookingStorage.save(booking);
        return newBooking;


    }

    public Booking approved(Long bookingId, Long userId, Boolean approved) {
        Booking newBooking = bookingStorage.findById(bookingId)
                .orElseThrow(() -> new NotFoundException("booking not found by id: " + bookingId));
        userStorage.findById(userId)
                .orElseThrow(() -> new NotFoundException("owner not found by id:" + userId));
        if (!newBooking.getItem().getOwner().getId().equals(userId)) {
            throw new NotFoundException("owner not found by id:" + userId);
        }
        if (newBooking.getStatus() != WAITING) {
            throw new BadRequestException("status error");
        }
        if (approved) {
            newBooking.setStatus(APPROVED);
        } else {
            newBooking.setStatus(REJECTED);
        }
        return bookingStorage.save(newBooking);
    }

    public Booking get(Long bookingId, Long userOrOwnerId) {
        Booking newBooking = bookingStorage.findById(bookingId)
                .orElseThrow(() -> new NotFoundException("booking not found by id: " + bookingId));
        if (newBooking.getItem().getOwner().getId().equals(userOrOwnerId) ||
                newBooking.getBooker().getId().equals(userOrOwnerId)) {
            return newBooking;
        } else {
            throw new NotFoundException("user or owner not found by id: " + userOrOwnerId);
        }
    }

    public List<Booking> getAll(Long bookerId, String state) {
        if (state == null || state.equals("ALL")) {
            return bookingStorage
                    .findByBooker_IdOrderByStartTimeDesc(bookerId);
        } else if (state.equals("CURRENT")) {
            return bookingStorage
                    .findByBooker_IdAndStartTimeBeforeAndEndTimeAfterOrderByStartTimeAsc(bookerId, LocalDateTime.now(), LocalDateTime.now());
        } else if (state.equals("PAST")) {
            return bookingStorage
                    .findByBooker_IdAndEndTimeBeforeOrderByStartTimeDesc(bookerId, LocalDateTime.now());
        } else if (state.equals("FUTURE")) {
            return bookingStorage
                    .findByBooker_IdAndStartTimeAfterOrderByStartTimeDesc(bookerId, LocalDateTime.now());
        } else if (state.equals("WAITING")) {
            return bookingStorage
                    .findByBooker_IdAndStatusOrderByStartTimeDesc(bookerId, WAITING);
        } else if (state.equals("REJECTED")) {
            return bookingStorage
                    .findByBooker_IdAndStatusOrderByStartTimeDesc(bookerId, REJECTED);
        } else {
            throw new InternalServerErrorException("Unknown state: " + state);
        }

    }

    public List<Booking> getAllOwnerId(Long ownerId, String state) {
        userStorage.findById(ownerId)
                .orElseThrow(() -> new InternalServerErrorException("owner not found by id:" + ownerId));
        if (state == null || state.equals("ALL")) {
            return bookingStorage.findByItem_OwnerIdOrderByStartTimeDesc(ownerId);
        } else if (state.equals("CURRENT")) {
            return bookingStorage
                    .findByItem_OwnerIdAndStartTimeBeforeAndEndTimeAfterOrderByStartTimeAsc(ownerId, LocalDateTime.now(), LocalDateTime.now());
        } else if (state.equals("PAST")) {
            return bookingStorage
                    .findByItem_OwnerIdAndEndTimeBeforeOrderByStartTimeDesc(ownerId, LocalDateTime.now());
        } else if (state.equals("FUTURE")) {
            return bookingStorage
                    .findByItem_OwnerIdAndStartTimeAfterOrderByStartTimeDesc(ownerId, LocalDateTime.now());
        } else if (state.equals("WAITING")) {
            return bookingStorage
                    .findByItem_OwnerIdAndStatusOrderByStartTimeDesc(ownerId, WAITING);
        } else if (state.equals("REJECTED")) {
            return bookingStorage
                    .findByItem_OwnerIdAndStatusOrderByStartTimeDesc(ownerId, REJECTED);
        } else {
            throw new InternalServerErrorException("Unknown state: " + state);
        }
    }

    public List<Booking> getAllOwnerIdFromAndSize(Long ownerId, Integer from, Integer size){
        userStorage.findById(ownerId)
                .orElseThrow(() -> new InternalServerErrorException("owner not found by id:" + ownerId));
        return bookingStorage.findByItem_OwnerIdOrderByStartTimeDesc(ownerId, PageRequest.of(from / size, size));
    }
    public List<Booking> getAllBookerIdFromAndSize(Long bookerId, Integer from, Integer size) {
        userStorage.findById(bookerId)
                .orElseThrow(() -> new InternalServerErrorException("owner not found by id:" + bookerId));
        return bookingStorage.findByBooker_IdOrderByStartTimeDesc(bookerId, PageRequest.of(from / size, size));
    }


}
