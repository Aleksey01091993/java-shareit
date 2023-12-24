package ru.practicum.shareit.booking;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.Nullable;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.booking.dto.BookingCreationDTO;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.mapper.BookingMapper;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.service.BookingService;

import java.util.List;
import java.util.stream.Collectors;


@RestController
@RequestMapping(path = "/bookings")
public class BookingController {
    private final BookingService service;

    public BookingController(
            @Autowired BookingService service
    ) {
        this.service = service;
    }

    @PostMapping
    public Booking createBookingDTO(
            @RequestBody @Valid @Nullable BookingCreationDTO bookingCreationDTO,
            @RequestHeader("X-Sharer-User-Id") Long userId
    ) {
        BookingDto bookingResponse = BookingMapper.toBookingDTO(service.create(bookingCreationDTO, userId));
        return bookingResponse;
    }

    @PatchMapping("/{bookingId}")
    public Booking approvedBookingDTO
            (
                    @RequestHeader("X-Sharer-User-Id") Long userId,
                    @PathVariable Long bookingId,
                    @RequestParam Boolean approved
            ) {
        BookingDto bookingResponse = BookingMapper.toBookingDTO(service.approved(bookingId, userId, approved));
        return bookingResponse;
    }

    @GetMapping("/{bookingId}")
    public Booking getBookingDTO
            (
                    @RequestHeader("X-Sharer-User-Id") Long userId,
                    @PathVariable Long bookingId
            ) {
        BookingDto bookingResponse = BookingMapper.toBookingDTO(service.get(bookingId, userId));
        return bookingResponse;
    }

    @GetMapping
    public List<Booking> gatAllBookingDTO
            (
                    @RequestHeader("X-Sharer-User-Id") Long userId,
                    @RequestParam @Nullable String state
            ) {
        return service.getAll(userId, state).stream()
                .map(BookingMapper::toBookingDTO)
                .collect(Collectors.toList());
    }

    @GetMapping("/owner")
    public List<Booking> getAllOwnerId
            (@RequestHeader("X-Sharer-User-Id") Long ownerId,
             @RequestParam @Nullable String state
            ) {
        return service.getAllOwnerId(ownerId, state).stream()
                .map(BookingMapper::toBookingDTO)
                .collect(Collectors.toList());
    }
}
