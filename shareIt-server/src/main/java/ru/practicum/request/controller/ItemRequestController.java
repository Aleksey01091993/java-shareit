package ru.practicum.request.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import ru.practicum.request.dto.ItemRequestCreateRequestDto;
import ru.practicum.request.dto.ItemRequestResponseDto;
import ru.practicum.request.service.ItemRequestService;

import java.util.List;


@Controller
@RequiredArgsConstructor
@Slf4j
@RequestMapping(path = "/requests")
public class ItemRequestController {
    private final ItemRequestService service;

    @PostMapping
    public ResponseEntity<Object> create(
            @RequestBody ItemRequestCreateRequestDto itemRequest,
            @RequestHeader("X-Sharer-User-Id") Long userId

    ) {
        log.info("Пришел POST запрос /requests с телом: {}", itemRequest);
        ItemRequestResponseDto itemRequestResponseDto = service.create(itemRequest, userId);
        log.info("Отправлен ответ для POST запроса /requests с телом: {}", itemRequestResponseDto);
        return new ResponseEntity<>(itemRequestResponseDto, HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<Object> findByAll(
            @RequestHeader("X-Sharer-User-Id") Long userId

    ) {
        log.info("Пришел GET запрос /requests с : X-Sharer-User-Id {}", userId);
        List<ItemRequestResponseDto> itemRequestResponseDto = service.findByAll(userId);
        log.info("Отправлен ответ для GET запроса /requests с телом: {}", itemRequestResponseDto);
        return new ResponseEntity<>(itemRequestResponseDto, HttpStatus.OK);
    }

    @GetMapping("/all")
    public ResponseEntity<Object> getAllFrom(
            @RequestHeader("X-Sharer-User-Id") Long userId,
            @RequestParam @Nullable Integer from,
            @RequestParam @Nullable Integer size
    ) {
        log.info("Пришел GET запрос /requests с параметрами: {}, {}, {}", userId, from, size);
        List<ItemRequestResponseDto> itemRequestResponseDto = service.getAllFrom(userId, from, size);
        log.info("Отправлен ответ для GET запроса /requests с телом: {}", itemRequestResponseDto);
        return new ResponseEntity<>(itemRequestResponseDto, HttpStatus.OK);
    }

    @GetMapping("/{requestId}")
    public ResponseEntity<Object> findById(
            @PathVariable Long requestId,
            @RequestHeader("X-Sharer-User-Id") Long userId
    ) {
        log.info("Пришел GET запрос /requests с параметрами: {}", requestId);
        ItemRequestResponseDto itemRequestResponseDto = service.findById(requestId, userId);
        log.info("Отправлен ответ для GET запроса /requests с телом: {}", itemRequestResponseDto);
        return new ResponseEntity<>(itemRequestResponseDto, HttpStatus.OK);
    }
}
