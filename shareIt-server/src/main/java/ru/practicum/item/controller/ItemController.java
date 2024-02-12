package ru.practicum.item.controller;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.item.coments.DTO.CommentsDTO;
import ru.practicum.item.dto.ItemCreateRequestDto;
import ru.practicum.item.dto.ItemResponseDto;
import ru.practicum.item.service.ItemService;

import java.util.List;


@Controller
@RequiredArgsConstructor
@Slf4j
@Validated
@RequestMapping("/items")
public class ItemController {

    private final ItemService service;

    @PostMapping
    public ResponseEntity<Object> create(
            @RequestBody ItemCreateRequestDto item,
            @RequestHeader("X-Sharer-User-Id") Long userId
    ) {
        log.info("Пришел POST запрос /items с телом: {}", item);
        ItemResponseDto itemResponseDto = service.create(item, userId);
        log.info("Отправлен ответ для POST запроса /items с телом: {}", itemResponseDto);
        return new ResponseEntity<>(itemResponseDto, HttpStatus.OK);
    }

    @PatchMapping("/{itemId}")
    public ResponseEntity<Object> update(
            @RequestBody @Nullable ItemCreateRequestDto item,
            @RequestHeader("X-Sharer-User-Id") Long userId,
            @PathVariable Long itemId
    ) {
        log.info("Пришел PATH запрос /items/{} с телом: {}", itemId, item);
        ItemResponseDto itemResponseDto = service.update(item, userId, itemId);
        log.info("Отправлен ответ для PATH запроса /items/{} с телом: {}", itemId, itemResponseDto);
        return new ResponseEntity<>(itemResponseDto, HttpStatus.OK);
    }

    @GetMapping("/{itemId}")
    public ResponseEntity<Object> get(
            @PathVariable Long itemId,
            @RequestHeader("X-Sharer-User-Id") Long userId
    ) {
        log.info("Пришел GET запрос /items/{}", itemId);
        ItemResponseDto itemResponseDto = service.get(itemId, userId);
                log.info("Отправлен ответ для GET запроса /items/{} с телом: {}", itemId, itemResponseDto);
        return new ResponseEntity<>(itemResponseDto, HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<Object> getAll(
            @RequestHeader("X-Sharer-User-Id") Long userId,
            @RequestParam @Nullable Integer from,
            @RequestParam @Nullable Integer size
    ) {
            log.info("Пришел GET запрос /items с параметрами: {}, {}", from, size);
        List<ItemResponseDto> itemResponseDto = service.getAll(userId, from, size);
            log.info("Отправлен ответ для GET запроса /items с параметрами: {}, {} с телом: {}", from, size, itemResponseDto);
        return new ResponseEntity<>(itemResponseDto, HttpStatus.OK);
    }

    @GetMapping("/search")
    public ResponseEntity<Object> getAllSearch(
            @RequestParam @Nullable String text,
            @RequestParam @Nullable Integer from,
            @RequestParam @Nullable Integer size
    ) {

            log.info("Пришел GET запрос /items с параметрами: {}, {}, {}", text, from, size);
        List<ItemResponseDto> itemResponseDto = service.getAllSearch(text, from, size);
            log.info("Отправлен ответ для GET запроса /items с параметрами: {}, {}, {} с телом: {}", text, from, size, itemResponseDto);
        return new ResponseEntity<>(itemResponseDto, HttpStatus.OK);
    }

    @PostMapping("/{itemId}/comment")
    public ResponseEntity<Object> addComment(
            @RequestHeader("X-Sharer-User-Id") Long userId,
            @PathVariable Long itemId,
            @RequestBody CommentsDTO commentsDTO
    ) {
        log.info("Пришел POST запрос /items/{}/comment с телом: {}", itemId, commentsDTO);
        CommentsDTO commentResponseDTO = service.addComment(userId, itemId, commentsDTO);
        log.info("Отправлен ответ для POST запроса /items/{}/comment с телом: {}", itemId, commentResponseDTO);
        return new ResponseEntity<>(commentResponseDTO, HttpStatus.OK);
    }


}
