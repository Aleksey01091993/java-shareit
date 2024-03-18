package ru.practicum.ItemRequest;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.ItemRequest.DTO.ItemRequestCreateRequestDto;

import javax.validation.Valid;
import javax.validation.constraints.PositiveOrZero;


@Controller
@RequiredArgsConstructor
@Slf4j
@Validated
@RequestMapping(path = "/requests")
public class ItemRequestController {
    private final ItemRequestClient client;

    @PostMapping()
    public ResponseEntity<Object> create
            (
                    @RequestBody @Valid ItemRequestCreateRequestDto requestDto,
                    @RequestHeader("X-Sharer-User-Id") Long userId
            ) {
        log.info("Пришел POST запрос /requests с телом: {}", requestDto);
        ResponseEntity<Object> itemRequestResponseDto = client.create(requestDto, userId);
        log.info("Отправлен ответ для POST запроса /requests с телом: {}", itemRequestResponseDto);
        return itemRequestResponseDto;
    }

    @GetMapping
    public ResponseEntity<Object> findAll
            (
                    @RequestHeader("X-Sharer-User-Id") Long userId
            ) {
        log.info("Пришел GET запрос /requests с : X-Sharer-User-Id {}", userId);
        ResponseEntity<Object> itemRequestResponseDto = client.findByAll(userId);
        log.info("Отправлен ответ для GET запроса /requests с телом: {}", itemRequestResponseDto);
        return itemRequestResponseDto;
    }

    @GetMapping("/all")
    public ResponseEntity<Object> findAllFrom
            (
                    @RequestHeader("X-Sharer-User-Id") Long userId,
                    @RequestParam @Nullable @PositiveOrZero Integer from,
                    @RequestParam @Nullable @PositiveOrZero Integer size
            ) {
        log.info("Пришел GET запрос /requests с параметрами: {}, {}, {}", userId, from, size);
        ResponseEntity<Object> itemRequestResponseDto = client.getAllFrom(userId, from, size);
        log.info("Отправлен ответ для GET запроса /requests с телом: {}", itemRequestResponseDto);
        return itemRequestResponseDto;
    }

    @GetMapping("/{requestId}")
    public ResponseEntity<Object> findById
            (
                    @PathVariable Long requestId,
                    @RequestHeader("X-Sharer-User-Id") Long userId
            ) {
        log.info("Пришел GET запрос /requests с параметрами: {}", requestId);
        ResponseEntity<Object> itemRequestResponseDto = client.findById(userId, requestId);
        log.info("Отправлен ответ для GET запроса /requests с телом: {}", itemRequestResponseDto);
        return itemRequestResponseDto;
    }
}
