package ru.practicum.controller;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.item.DTO.CommentsDTO;
import ru.practicum.item.DTO.ItemCreateRequestDto;

import javax.validation.Valid;


@Controller
@RequiredArgsConstructor
@Slf4j
@Validated
@RequestMapping("/items")
public class ItemController {

    private final ItemClient client;

    @PostMapping
    public ResponseEntity<Object> itemCreateRequest
            (
                    @RequestBody @Valid ItemCreateRequestDto item,
                    @RequestHeader("X-Sharer-User-Id") Long userId
            ) {
        log.info("Пришел POST запрос /items с телом: {}", item);
        ResponseEntity<Object> itemResponseDto = client.itemCreate(item, userId);
        log.info("Отправлен ответ для POST запроса /items с телом: {}", itemResponseDto);
        return itemResponseDto;
    }

    @PatchMapping("/{itemId}")
    public ResponseEntity<Object> itemUpdate
            (
                    @RequestBody @Nullable ItemCreateRequestDto item,
                    @RequestHeader("X-Sharer-User-Id") Long userId,
                    @PathVariable Long itemId
            ) {
        log.info("Пришел PATH запрос /items/{} с телом: {}", itemId, item);
        ResponseEntity<Object> itemResponseDto = client.itemUpdate(item, userId, itemId);
        log.info("Отправлен ответ для PATH запроса /items/{} с телом: {}", itemId, itemResponseDto);
        return itemResponseDto;
    }

    @GetMapping("/{itemId}")
    public ResponseEntity<Object> itemGet
            (
                    @PathVariable Long itemId,
                    @RequestHeader("X-Sharer-User-Id") Long userId
            ) {
        log.info("Пришел GET запрос /items/{}", itemId);
        ResponseEntity<Object> itemResponseDto = client.itemGet(userId, itemId);
                log.info("Отправлен ответ для GET запроса /items/{} с телом: {}", itemId, itemResponseDto);
        return itemResponseDto;
    }

    @GetMapping
    public ResponseEntity<Object> itemsGetAll
            (
                    @RequestHeader("X-Sharer-User-Id") Long userId,
                    @RequestParam @Nullable Integer from,
                    @RequestParam @Nullable Integer size
            ) {
            log.info("Пришел GET запрос /items с параметрами: {}, {}", from, size);
        ResponseEntity<Object> itemResponseDto = client.itemsGetAll(userId, from, size);
            log.info("Отправлен ответ для GET запроса /items с параметрами: {}, {} с телом: {}", from, size, itemResponseDto);
            return itemResponseDto;
    }

    @GetMapping("/search")
    public ResponseEntity<Object> itemsGetAllSearch
            (
                    @RequestParam @Nullable String text,
                    @RequestParam @Nullable Integer from,
                    @RequestParam @Nullable Integer size
            ) {

            log.info("Пришел GET запрос /items с параметрами: {}, {}, {}", text, from, size);
        ResponseEntity<Object> itemResponseDto = client.itemsGetAllSearch(text, from, size);
            log.info("Отправлен ответ для GET запроса /items с параметрами: {}, {}, {} с телом: {}", text, from, size, itemResponseDto);
            return itemResponseDto;
    }

    @PostMapping("/{itemId}/comment")
    public ResponseEntity<Object> addComments
            (
                    @RequestHeader("X-Sharer-User-Id") Long userId,
                    @PathVariable Long itemId,
                    @RequestBody @Valid CommentsDTO commentsDTO
            ) {
        log.info("Пришел POST запрос /items/{}/comment с телом: {}", itemId, commentsDTO);
        ResponseEntity<Object> commentResponseDTO = client.addComments(userId, itemId, commentsDTO);
        log.info("Отправлен ответ для POST запроса /items/{}/comment с телом: {}", itemId, commentResponseDTO);
        return commentResponseDTO;
    }


}
