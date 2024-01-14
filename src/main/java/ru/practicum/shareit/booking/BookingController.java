package ru.practicum.shareit.booking;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.Nullable;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.booking.dto.BookingResponseDTO;
import ru.practicum.shareit.booking.dto.BookingRequestDto;
import ru.practicum.shareit.booking.mapper.BookingMapper;
import ru.practicum.shareit.booking.service.BookingService;

import java.util.List;
import java.util.stream.Collectors;


@RestController
@Slf4j
@RequestMapping(path = "/bookings")
public class BookingController {
    private final BookingService service;

    public BookingController(@Autowired BookingService service) {
        this.service = service;
    }

    @PostMapping
    public BookingResponseDTO createBookingDTO(
            @RequestBody @Valid @Nullable BookingRequestDto bookingRequestDto,
            @RequestHeader("X-Sharer-User-Id") Long userId
    ) {
        log.info("Пришел POST запрос /bookings с телом: {}", bookingRequestDto);
        BookingResponseDTO bookingResponseDto = BookingMapper.toBookingDto(service.create(bookingRequestDto, userId));
        log.info("Отправлен ответ для POST запроса /bookings с телом: {}", bookingResponseDto);
        return bookingResponseDto;
    }

    @PatchMapping("/{bookingId}")
    public BookingResponseDTO approvedBookingDTO
            (
                    @RequestHeader("X-Sharer-User-Id") Long userId,
                    @PathVariable Long bookingId,
                    @RequestParam Boolean approved
            ) {
        log.info("Пришел POST запрос /bookings/{} с телом: {}", bookingId, approved);
        BookingResponseDTO bookingResponseDto = BookingMapper.toBookingDto(service.approved(bookingId, userId, approved));
        log.info("Отправлен ответ для PATH запроса /bookings/{} с телом: {}", bookingId, bookingResponseDto);
        return bookingResponseDto;
    }

    @GetMapping("/{bookingId}")
    public BookingResponseDTO getBookingDTO
            (
                    @RequestHeader("X-Sharer-User-Id") Long userId,
                    @PathVariable Long bookingId
            ) {
        log.info("Пришел GET запрос /bookings/{}", bookingId);
        BookingResponseDTO bookingResponseDto = BookingMapper.toBookingDto(service.get(bookingId, userId));
        log.info("Отправлен ответ для GET запроса /bookings/{} с телом: {}", bookingId, bookingResponseDto);
        return bookingResponseDto;
    }

    @GetMapping
    public List<BookingResponseDTO> gatAllBookingDTO
            (
                    @RequestHeader("X-Sharer-User-Id") Long userId,
                    @RequestParam @Nullable String state,
                    @RequestParam @Nullable Integer from,
                    @RequestParam @Nullable Integer size
            ) {
        if (from != null && size != null) {
            log.info("Пришел GET запрос /bookings?from={}&size={}", from, size);
            List<BookingResponseDTO> bookingResponseDto = service.getAllBookerIdFromAndSize(userId, from, size).stream()
                    .map(BookingMapper::toBookingDto)
                    .collect(Collectors.toList());
            log.info("Отправлен ответ для GET запроса /bookings?from={}&size={} с телом: {}", from, size, bookingResponseDto);
            return bookingResponseDto;
        } else {
            log.info("Пришел GET запрос /bookings?state={}", state);
            List<BookingResponseDTO> bookingResponseDto = service.getAll(userId, state).stream()
                    .map(BookingMapper::toBookingDto)
                    .collect(Collectors.toList());
            log.info("Отправлен ответ для GET запроса /bookings?state={} с телом: {}", state, bookingResponseDto);
            return bookingResponseDto;
        }

    }

    @GetMapping("/owner")
    public List<BookingResponseDTO> getAllOwnerId
            (@RequestHeader("X-Sharer-User-Id") Long ownerId,
             @RequestParam @Nullable String state,
             @RequestParam @Nullable Integer from,
             @RequestParam @Nullable Integer size
            ) {
        if (from != null && size != null) {
            log.info("Пришел GET запрос /bookings/owner?from={}&size={}", from, size);
            List<BookingResponseDTO> bookingResponseDto = service.getAllOwnerIdFromAndSize(ownerId, from, size).stream()
                    .map(BookingMapper::toBookingDto)
                    .collect(Collectors.toList());
            log.info("Отправлен ответ для GET запроса /bookings/owner?from={}&size={} с телом: {}", from, size, bookingResponseDto);
            return bookingResponseDto;
        } else {
            log.info("Пришел GET запрос /bookings/owner?state={}", state);
            List<BookingResponseDTO> bookingResponseDto = service.getAllOwnerId(ownerId, state).stream()
                    .map(BookingMapper::toBookingDto)
                    .collect(Collectors.toList());
            log.info("Отправлен ответ для GET запроса /bookings/owner?state={} с телом: {}", state, bookingResponseDto);
            return bookingResponseDto;
        }
    }

}
