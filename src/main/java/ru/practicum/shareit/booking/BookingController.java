package ru.practicum.shareit.booking;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
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
@Slf4j
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
        log.info("Пришел POST запрос /bookings с телом: {}", bookingResponseDTO);
        BookingDTO bookingResponseDto = BookingMapper.toBookingDto(service.create(bookingResponseDTO, userId));
        log.info("Отправлен ответ для POST запроса /bookings с телом: {}", bookingResponseDto);
        return bookingResponseDto;
    }

    @PatchMapping("/{bookingId}")
    public BookingDTO approvedBookingDTO
            (
                    @RequestHeader("X-Sharer-User-Id") Long userId,
                    @PathVariable Long bookingId,
                    @RequestParam Boolean approved
            ) {
        log.info("Пришел POST запрос /bookings/{} с телом: {}", bookingId, approved);
        BookingDTO bookingResponseDto = BookingMapper.toBookingDto(service.approved(bookingId, userId, approved));
        log.info("Отправлен ответ для PATH запроса /bookings/{} с телом: {}", bookingId, bookingResponseDto);
        return bookingResponseDto;
    }

    @GetMapping("/{bookingId}")
    public BookingDTO getBookingDTO
            (
                    @RequestHeader("X-Sharer-User-Id") Long userId,
                    @PathVariable Long bookingId
            ) {
        log.info("Пришел GET запрос /bookings/{}", bookingId);
        BookingDTO bookingResponseDto = BookingMapper.toBookingDto(service.get(bookingId, userId));
        log.info("Отправлен ответ для GET запроса /bookings/{} с телом: {}", bookingId, bookingResponseDto);
        return bookingResponseDto;
    }

    @GetMapping
    public List<BookingDTO> gatAllBookingDTO
            (
                    @RequestHeader("X-Sharer-User-Id") Long userId,
                    @RequestParam @Nullable String state
            ) {
        log.info("Пришел GET запрос /bookings?state={}", state);
        List<BookingDTO> bookingResponseDto = service.getAll(userId, state).stream()
                .map(BookingMapper::toBookingDto)
                .collect(Collectors.toList());
        log.info("Отправлен ответ для GET запроса /bookings?state={} с телом: {}", state, bookingResponseDto);
        return bookingResponseDto;
    }

    @GetMapping("/owner")
    public List<BookingDTO> getAllOwnerId
            (@RequestHeader("X-Sharer-User-Id") Long ownerId,
             @RequestParam @Nullable String state
            ) {
        log.info("Пришел GET запрос /bookings/owner?state={}", state);
        List<BookingDTO> bookingResponseDto = service.getAllOwnerId(ownerId, state).stream()
                .map(BookingMapper::toBookingDto)
                .collect(Collectors.toList());
        log.info("Отправлен ответ для GET запроса /bookings/owner?state={} с телом: {}", state, bookingResponseDto);
        return bookingResponseDto;
    }

}
