package ru.practicum.shareit.item;

import jakarta.annotation.Nullable;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.item.coments.DTO.CommentsDTO;
import ru.practicum.shareit.item.coments.mapper.CommentMapper;
import ru.practicum.shareit.item.dto.ItemCreateRequestDto;
import ru.practicum.shareit.item.dto.ItemResponseDto;
import ru.practicum.shareit.item.mapper.ItemMapper;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.service.ItemService;

import java.util.List;
import java.util.stream.Collectors;


@Component
@Slf4j
@RestController
@RequestMapping("/items")
public class ItemController {

    private final ItemService service;

    @Autowired
    public ItemController(ItemService service) {
        this.service = service;
    }

    @PostMapping
    public ItemResponseDto itemCreateRequestDTO
            (
                    @RequestBody @Valid ItemCreateRequestDto item,
                    @RequestHeader("X-Sharer-User-Id") Long userId
            ) {
        log.info("Пришел POST запрос /items с телом: {}", item);
        ItemResponseDto itemResponseDto = ItemMapper.toItemDTO(service.create(item, userId),  null);
        log.info("Отправлен ответ для POST запроса /items с телом: {}", itemResponseDto);
        return itemResponseDto;
    }

    @PatchMapping("/{itemId}")
    public ItemResponseDto itemUpdateDTO
            (
                    @RequestBody @Nullable ItemCreateRequestDto item,
                    @RequestHeader("X-Sharer-User-Id") Long userId,
                    @PathVariable Long itemId
            ) {
        log.info("Пришел PATH запрос /items/{} с телом: {}", itemId, item);
        ItemResponseDto itemResponseDto = ItemMapper.toItemDTO(service.update(item, userId, itemId), null);
        log.info("Отправлен ответ для PATH запроса /items/{} с телом: {}", itemId, itemResponseDto);
        return itemResponseDto;
    }

    @GetMapping("/{itemId}")
    public ItemResponseDto itemGetDTO
            (
                    @PathVariable Long itemId,
                    @RequestHeader("X-Sharer-User-Id") Long userId
            ) {
        log.info("Пришел GET запрос /items/{}", itemId);
        Item item = service.get(itemId, userId);
        ItemResponseDto itemResponseDto = ItemMapper.toItemDTO(item, item.getComments());
        log.info("Отправлен ответ для GET запроса /items/{} с телом: {}", itemId, itemResponseDto);
        return itemResponseDto;
    }

    @GetMapping
    public List<ItemResponseDto> itemsGetAllDTO
            (
                    @RequestHeader("X-Sharer-User-Id") Long userId,
                    @RequestParam @Nullable Integer from,
                    @RequestParam @Nullable Integer size
            ) {
            log.info("Пришел GET запрос /items с параметрами: {}, {}", from, size);
            List<Item> itemDto = service.getAll(userId, from, size);
            List<ItemResponseDto> itemResponseDto = itemDto.stream()
                    .map(o1 -> ItemMapper.toItemDTO(o1, o1.getComments()))
                    .collect(Collectors.toList());
            log.info("Отправлен ответ для GET запроса /items с параметрами: {}, {} с телом: {}", from, size, itemResponseDto);
            return itemResponseDto;
    }

    @GetMapping("/search")
    public List<ItemResponseDto> itemsGetAllSearchDTO
            (
                    @RequestParam @Nullable String text,
                    @RequestParam @Nullable Integer from,
                    @RequestParam @Nullable Integer size
            ) {

            log.info("Пришел GET запрос /items с параметрами: {}, {}, {}", text, from, size);
            List<Item> itemDto = service.getAllSearch(text, from, size);
            List<ItemResponseDto> itemResponseDto = itemDto.stream()
                    .map(o1 -> ItemMapper.toItemDTO(o1, o1.getComments()))
                    .collect(Collectors.toList());
            log.info("Отправлен ответ для GET запроса /items с параметрами: {}, {}, {} с телом: {}", text, from, size, itemResponseDto);
            return itemResponseDto;
    }

    @PostMapping("/{itemId}/comment")
    public CommentsDTO addCommentsDTO
            (
                    @RequestHeader("X-Sharer-User-Id") Long userId,
                    @PathVariable Long itemId,
                    @RequestBody @Valid CommentsDTO commentsDTO
            ) {
        log.info("Пришел POST запрос /items/{}/comment с телом: {}", itemId, commentsDTO);
        CommentsDTO commentResponseDTO = CommentMapper.toCommentsDTO(service.addComment(userId, itemId, commentsDTO));
        log.info("Отправлен ответ для POST запроса /items/{}/comment с телом: {}", itemId, commentResponseDTO);
        return commentResponseDTO;
    }


}
