package ru.practicum.shareit.booking;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.Nullable;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.booking.dto.BookingDTO;
import ru.practicum.shareit.booking.dto.BookingResponseDTO;
import ru.practicum.shareit.booking.mapper.BookingMapper;
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
    public BookingDTO createBookingDTO(
            @RequestBody @Valid @Nullable BookingResponseDTO bookingResponseDTO,
            @RequestHeader("X-Sharer-User-Id") Long userId
    ) {
        BookingDTO bookingResponseDto = BookingMapper.toBookingDto(service.create(bookingResponseDTO, userId));
        return bookingResponseDto;
    }

    @PatchMapping("/{bookingId}")
    public BookingDTO approvedBookingDTO
            (
                    @RequestHeader("X-Sharer-User-Id") Long userId,
                    @PathVariable Long bookingId,
                    @RequestParam Boolean approved
            ) {
        BookingDTO bookingResponseDto = BookingMapper.toBookingDto(service.approved(bookingId, userId, approved));
        return bookingResponseDto;
    }

    @GetMapping("/{bookingId}")
    public BookingDTO getBookingDTO
            (
                    @RequestHeader("X-Sharer-User-Id") Long userId,
                    @PathVariable Long bookingId
            ) {
        BookingDTO bookingResponseDto = BookingMapper.toBookingDto(service.get(bookingId, userId));
        return bookingResponseDto;
    }

    @GetMapping
    public List<BookingDTO> gatAllBookingDTO
            (
                    @RequestHeader("X-Sharer-User-Id") Long userId,
                    @RequestParam @Nullable String state
            ) {
        List<BookingDTO> bookingResponseDto = service.getAll(userId, state).stream()
                .map(BookingMapper::toBookingDto)
                .collect(Collectors.toList());
        return bookingResponseDto;
    }

    @GetMapping("/owner")
    public List<BookingDTO> getAllOwnerId
            (@RequestHeader("X-Sharer-User-Id") Long ownerId,
             @RequestParam @Nullable String state
            ) {
        List<BookingDTO> bookingResponseDto = service.getAllOwnerId(ownerId, state).stream()
                .map(BookingMapper::toBookingDto)
                .collect(Collectors.toList());
        return bookingResponseDto;
    }
}
