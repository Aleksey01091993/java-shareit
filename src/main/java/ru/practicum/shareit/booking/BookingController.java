package ru.practicum.shareit.booking;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.Nullable;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.booking.dto.BookingDTO;
import ru.practicum.shareit.booking.dto.BookingCreationDTO;
import ru.practicum.shareit.booking.dto.BookingAndTimeDTO;
import ru.practicum.shareit.booking.mapper.BookingMapper;
import ru.practicum.shareit.booking.service.BookingService;

import java.util.List;


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
    public BookingAndTimeDTO createBookingDTO(
            @RequestBody @Valid @Nullable BookingCreationDTO bookingCreationDTO,
            @RequestHeader("X-Sharer-User-Id") Long userId
    ) {
        return BookingMapper.toBookingAndTimeDTO(service.create(bookingCreationDTO, userId));
    }

    @PatchMapping("/{bookingId}")
    public BookingAndTimeDTO approvedBookingDTO
            (
                    @RequestHeader("X-Sharer-User-Id") Long userId,
                    @PathVariable Long bookingId,
                    @RequestParam Boolean approved
            ) {
        return BookingMapper.toBookingAndTimeDTO(service.approved(bookingId, userId, approved));
    }

    @GetMapping("/{bookingId}")
    public BookingAndTimeDTO getBookingDTO
            (
                    @RequestHeader("X-Sharer-User-Id") Long userId,
                    @PathVariable Long bookingId
            ) {
        return BookingMapper.toBookingAndTimeDTO(service.get(bookingId, userId));
    }

    @GetMapping
    public List<BookingDTO> gatAllBookingDTO
            (
                    @RequestHeader("X-Sharer-User-Id") Long userId,
                    @RequestParam @Nullable String state
            ) {
        return service.getAll(userId, state);
    }

    @GetMapping("/owner")
    public List<BookingDTO> getAllOwnerId
            (@RequestHeader("X-Sharer-User-Id") Long ownerId,
             @RequestParam @Nullable String state
            ) {
        return service.getAllOwnerId(ownerId, state);
    }
}
