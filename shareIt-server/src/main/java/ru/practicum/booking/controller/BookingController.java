package ru.practicum.booking.controller;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.properties.bind.DefaultValue;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import ru.practicum.booking.dto.BookingRequestDto;
import ru.practicum.booking.dto.BookingResponseDTO;
import ru.practicum.booking.service.BookingService;

import java.util.List;


@Controller
@RequiredArgsConstructor
@Slf4j
@RequestMapping(path = "/bookings")
public class BookingController {
    private final BookingService service;


    @PostMapping
    public ResponseEntity<Object> create(
            @RequestBody @Nullable BookingRequestDto bookingRequestDto,
            @RequestHeader("X-Sharer-User-Id") Long userId
    ) {
        log.info("Пришел POST запрос /bookings с телом: {}", bookingRequestDto);
        BookingResponseDTO bookingResponseDto = service.create(bookingRequestDto, userId);
        log.info("Отправлен ответ для POST запроса /bookings с телом: {}", bookingResponseDto);
        return new ResponseEntity<>(bookingResponseDto, HttpStatus.OK);
    }

    @PatchMapping("/{bookingId}")
    public ResponseEntity<Object> approved(
            @RequestHeader("X-Sharer-User-Id") Long userId,
            @PathVariable Long bookingId,
            @RequestParam @DefaultValue(value = "null") String approved
    ) {
        log.info("Пришел POST запрос /bookings/{} с телом: {}", bookingId, approved);
        BookingResponseDTO bookingResponseDto = service.approved(userId, bookingId, approved);
        log.info("Отправлен ответ для PATH запроса /bookings/{} с телом: {}", bookingId, bookingResponseDto);
        return new ResponseEntity<>(bookingResponseDto, HttpStatus.OK);
    }

    @GetMapping("/{bookingId}")
    public ResponseEntity<Object> get(
            @RequestHeader("X-Sharer-User-Id") Long userId,
            @PathVariable Long bookingId
    ) {
        log.info("Пришел GET запрос /bookings/{}", bookingId);
        BookingResponseDTO bookingResponseDto = service.get(userId, bookingId);
        log.info("Отправлен ответ для GET запроса /bookings/{} с телом: {}", bookingId, bookingResponseDto);
        return new ResponseEntity<>(bookingResponseDto, HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<Object> getAll(
            @RequestHeader("X-Sharer-User-Id") Long userId,
            @RequestParam @Nullable String state,
            @RequestParam @Nullable Integer from,
            @RequestParam @Nullable Integer size
    ) {
        log.info("Пришел GET запрос /bookings с параметрами {}, {}, {}", state, from, size);
        List<BookingResponseDTO> bookingResponseDto = service.getAll(userId, state, from, size);
        log.info("Отправлен ответ для GET запроса /bookings с параметрами {}, {}, {}, с телом: {}", state, from, size, bookingResponseDto);
        return new ResponseEntity<>(bookingResponseDto, HttpStatus.OK);
    }


    @GetMapping("/owner")
    public ResponseEntity<Object> getAllOwnerId(
            @RequestHeader("X-Sharer-User-Id") Long ownerId,
            @RequestParam @Nullable String state,
            @RequestParam @Nullable Integer from,
            @RequestParam @Nullable Integer size
    ) {
        log.info("Пришел GET запрос /bookings с параметрами: {}, {}, {}", state, from, size);
        List<BookingResponseDTO> bookingResponseDto = service.getAllOwnerId(ownerId, state, from, size);
        log.info("Отправлен ответ для GET запроса /bookings с параметрами: {}, {}, {}, с телом: {}", state, from, size, bookingResponseDto);
        return new ResponseEntity<>(bookingResponseDto, HttpStatus.OK);
    }
}
