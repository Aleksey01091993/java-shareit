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
}
