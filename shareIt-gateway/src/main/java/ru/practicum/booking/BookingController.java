package ru.practicum.booking;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.properties.bind.DefaultValue;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.booking.DTO.BookingRequestDto;

import javax.validation.Valid;


@Controller
@RequiredArgsConstructor
@Slf4j
@Validated
@RequestMapping(path = "/bookings")
public class BookingController {
    private final BookingClient client;


    @PostMapping
    public ResponseEntity<Object> createBooking
            (
                    @RequestBody @Valid @Nullable BookingRequestDto bookingRequestDto,
                    @RequestHeader("X-Sharer-User-Id") Long userId
            ) {
        log.info("Пришел POST запрос /bookings с телом: {}", bookingRequestDto);
        ResponseEntity<Object> bookingResponseDto = client.createBooking(bookingRequestDto, userId);
        log.info("Отправлен ответ для POST запроса /bookings с телом: {}", bookingResponseDto);
        return bookingResponseDto;
    }

    @PatchMapping("/{bookingId}")
    public ResponseEntity<Object> approvedBooking
            (
                    @RequestHeader("X-Sharer-User-Id") Long userId,
                    @PathVariable Long bookingId,
                    @RequestParam @DefaultValue(value = "null") String approved
            ) {
        log.info("Пришел POST запрос /bookings/{} с телом: {}", bookingId, approved);
        ResponseEntity<Object> bookingResponseDto = client.approvedBooking(userId, bookingId, approved);
        log.info("Отправлен ответ для PATH запроса /bookings/{} с телом: {}", bookingId, bookingResponseDto);
        return bookingResponseDto;
    }

    @GetMapping("/{bookingId}")
    public ResponseEntity<Object> getBooking
            (
                    @RequestHeader("X-Sharer-User-Id") Long userId,
                    @PathVariable Long bookingId
            ) {
        log.info("Пришел GET запрос /bookings/{}", bookingId);
        ResponseEntity<Object> bookingResponseDto = client.getBooking(userId, bookingId);
        log.info("Отправлен ответ для GET запроса /bookings/{} с телом: {}", bookingId, bookingResponseDto);
        return bookingResponseDto;
    }

    @GetMapping
    public ResponseEntity<Object> gatAllBooking
            (
                    @RequestHeader("X-Sharer-User-Id") Long userId,
                    @RequestParam @Nullable String state,
                    @RequestParam @Nullable Integer from,
                    @RequestParam @Nullable Integer size
            ) {
            log.info("Пришел GET запрос /bookings с параметрами {}, {}, {}", state, from, size);
        ResponseEntity<Object> bookingResponseDto = client.gatAllBooking(userId, state, from, size);
            log.info("Отправлен ответ для GET запроса /bookings с параметрами {}, {}, {}, с телом: {}", state, from, size, bookingResponseDto);
            return bookingResponseDto;
        }


    @GetMapping("/owner")
    public ResponseEntity<Object> getAllOwnerId
            (
                    @RequestHeader("X-Sharer-User-Id") Long ownerId,
                    @RequestParam @Nullable String state,
                    @RequestParam @Nullable Integer from,
                    @RequestParam @Nullable Integer size
            ) {
            log.info("Пришел GET запрос /bookings с параметрами: {}, {}, {}", state, from, size);
        ResponseEntity<Object> bookingResponseDto = client.getAllOwnerId(ownerId, state, from, size);
            log.info("Отправлен ответ для GET запроса /bookings с параметрами: {}, {}, {}, с телом: {}", state, from, size, bookingResponseDto);
            return bookingResponseDto;
        }
    }
