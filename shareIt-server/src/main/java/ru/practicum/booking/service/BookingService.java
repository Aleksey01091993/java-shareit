package ru.practicum.booking.service;


import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Controller;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.*;
import ru.practicum.booking.dto.BookingRequestDto;
import ru.practicum.booking.dto.BookingResponseDTO;
import ru.practicum.booking.mapper.BookingMapper;
import ru.practicum.booking.model.Booking;
import ru.practicum.booking.storage.BookingStorage;
import ru.practicum.exception.BadRequestException;
import ru.practicum.exception.InternalServerErrorException;
import ru.practicum.exception.NotFoundException;
import ru.practicum.item.model.Item;
import ru.practicum.item.storage.ItemStorage;
import ru.practicum.user.model.User;
import ru.practicum.user.storage.UserStorage;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;


import static ru.practicum.booking.status.Status.*;

@Service
@Controller
@RequestMapping(path = "/bookings")
@RequiredArgsConstructor
public class BookingService {
    private final UserStorage userStorage;
    private final ItemStorage itemStorage;
    private final BookingStorage bookingStorage;


    @PostMapping
    public BookingResponseDTO create(
            @RequestBody @Valid @Nullable BookingRequestDto bookingRequestDto,
            @RequestHeader("X-Sharer-User-Id") Long userId
    ) {
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
        return BookingMapper.toBookingDto(newBooking);


    }
    @PatchMapping("/{bookingId}")
    public BookingResponseDTO approved(
            @RequestHeader("X-Sharer-User-Id") Long userId,
            @PathVariable Long bookingId,
            @RequestParam Boolean approved
    ) {
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
        Booking saveBooking = bookingStorage.save(newBooking);
        return BookingMapper.toBookingDto(saveBooking);
    }
    @GetMapping("/{bookingId}")
    public BookingResponseDTO get(
            @RequestHeader("X-Sharer-User-Id") Long userId,
            @PathVariable Long bookingId
    ) {
        Booking newBooking = bookingStorage.findById(bookingId)
                .orElseThrow(() -> new NotFoundException("booking not found by id: " + bookingId));
        if (newBooking.getItem().getOwner().getId().equals(userId) ||
                newBooking.getBooker().getId().equals(userId)) {
            return BookingMapper.toBookingDto(newBooking);
        } else {
            throw new NotFoundException("user or owner not found by id: " + userId);
        }
    }
    @GetMapping
    public List<BookingResponseDTO> getAll(
            @RequestHeader("X-Sharer-User-Id") Long userId,
            @RequestParam @Nullable String state,
            @RequestParam @Nullable Integer from,
            @RequestParam @Nullable Integer size
    ) {
        if (from != null && size != null) {
            userStorage.findById(userId)
                    .orElseThrow(() -> new InternalServerErrorException("owner not found by id:" + userId));
            return bookingStorage.findByBooker_IdOrderByStartTimeDesc(userId, PageRequest.of(from / size, size))
                    .stream()
                    .map(BookingMapper::toBookingDto)
                    .collect(Collectors.toList());
        }
        if (state == null || state.equals("ALL")) {
            return bookingStorage
                    .findByBooker_IdOrderByStartTimeDesc(userId)
                    .stream()
                    .map(BookingMapper::toBookingDto)
                    .collect(Collectors.toList());
        } else if (state.equals("CURRENT")) {
            return bookingStorage
                    .findByBooker_IdAndStartTimeBeforeAndEndTimeAfterOrderByStartTimeAsc(userId, LocalDateTime.now(), LocalDateTime.now())
                    .stream()
                    .map(BookingMapper::toBookingDto)
                    .collect(Collectors.toList());
        } else if (state.equals("PAST")) {
            return bookingStorage
                    .findByBooker_IdAndEndTimeBeforeOrderByStartTimeDesc(userId, LocalDateTime.now())
                    .stream()
                    .map(BookingMapper::toBookingDto)
                    .collect(Collectors.toList());
        } else if (state.equals("FUTURE")) {
            return bookingStorage
                    .findByBooker_IdAndStartTimeAfterOrderByStartTimeDesc(userId, LocalDateTime.now())
                    .stream()
                    .map(BookingMapper::toBookingDto)
                    .collect(Collectors.toList());
        } else if (state.equals("WAITING")) {
            return bookingStorage
                    .findByBooker_IdAndStatusOrderByStartTimeDesc(userId, WAITING)
                    .stream()
                    .map(BookingMapper::toBookingDto)
                    .collect(Collectors.toList());
        } else if (state.equals("REJECTED")) {
            return bookingStorage
                    .findByBooker_IdAndStatusOrderByStartTimeDesc(userId, REJECTED)
                    .stream()
                    .map(BookingMapper::toBookingDto)
                    .collect(Collectors.toList());
        } else {
            throw new InternalServerErrorException("Unknown state: " + state);
        }

    }
    @GetMapping("/owner")
    public List<BookingResponseDTO> getAllOwnerId(
            @RequestHeader("X-Sharer-User-Id") Long ownerId,
            @RequestParam @Nullable String state,
            @RequestParam @Nullable Integer from,
            @RequestParam @Nullable Integer size
    ) {
        if (from != null && size != null) {
            userStorage.findById(ownerId)
                    .orElseThrow(() -> new InternalServerErrorException("owner not found by id:" + ownerId));
            return bookingStorage.findByItem_OwnerIdOrderByStartTimeDesc(ownerId, PageRequest.of(from / size, size))
                    .stream()
                    .map(BookingMapper::toBookingDto)
                    .collect(Collectors.toList());
        }
        userStorage.findById(ownerId)
                .orElseThrow(() -> new InternalServerErrorException("owner not found by id:" + ownerId));
        if (state == null || state.equals("ALL")) {
            return bookingStorage.findByItem_OwnerIdOrderByStartTimeDesc(ownerId)
                    .stream()
                    .map(BookingMapper::toBookingDto)
                    .collect(Collectors.toList());
        } else if (state.equals("CURRENT")) {
            return bookingStorage
                    .findByItem_OwnerIdAndStartTimeBeforeAndEndTimeAfterOrderByStartTimeAsc(ownerId, LocalDateTime.now(), LocalDateTime.now())
                    .stream()
                    .map(BookingMapper::toBookingDto)
                    .collect(Collectors.toList());
        } else if (state.equals("PAST")) {
            return bookingStorage
                    .findByItem_OwnerIdAndEndTimeBeforeOrderByStartTimeDesc(ownerId, LocalDateTime.now())
                    .stream()
                    .map(BookingMapper::toBookingDto)
                    .collect(Collectors.toList());
        } else if (state.equals("FUTURE")) {
            return bookingStorage
                    .findByItem_OwnerIdAndStartTimeAfterOrderByStartTimeDesc(ownerId, LocalDateTime.now())
                    .stream()
                    .map(BookingMapper::toBookingDto)
                    .collect(Collectors.toList());
        } else if (state.equals("WAITING")) {
            return bookingStorage
                    .findByItem_OwnerIdAndStatusOrderByStartTimeDesc(ownerId, WAITING)
                    .stream()
                    .map(BookingMapper::toBookingDto)
                    .collect(Collectors.toList());
        } else if (state.equals("REJECTED")) {
            return bookingStorage
                    .findByItem_OwnerIdAndStatusOrderByStartTimeDesc(ownerId, REJECTED)
                    .stream()
                    .map(BookingMapper::toBookingDto)
                    .collect(Collectors.toList());
        } else {
            throw new InternalServerErrorException("Unknown state: " + state);
        }
    }
}
