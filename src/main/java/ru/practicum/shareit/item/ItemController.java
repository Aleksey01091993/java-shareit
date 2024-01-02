package ru.practicum.shareit.item;

import jakarta.annotation.Nullable;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.item.coments.DTO.CommentsDTO;
import ru.practicum.shareit.item.coments.mapper.CommentMapper;
import ru.practicum.shareit.item.dto.ItemDTO;
import ru.practicum.shareit.item.dto.ItemResponseDTO;
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
    public ItemDTO itemCreateRequestDTO(@RequestBody @Valid ItemResponseDTO item,
                                        @RequestHeader("X-Sharer-User-Id") Long userId)
    {
        log.info("Пришел POST запрос /items с телом: {}", item);
        ItemDTO itemResponseDto = ItemMapper.toItemDTO(service.create(item, userId), null, null, null);
        log.info("Отправлен ответ для POST запроса /items с телом: {}", itemResponseDto);
        return itemResponseDto;
    }

    @PatchMapping("/{itemId}")
    public ItemDTO itemUpdateDTO(@RequestBody @Nullable ItemResponseDTO item,
                          @RequestHeader("X-Sharer-User-Id") Long userId,
                          @PathVariable Long itemId)
    {
        log.info("Пришел PATH запрос /items/{} с телом: {}", itemId, item);
        ItemDTO itemResponseDto = ItemMapper.toItemDTO(service.update(item, userId, itemId), null, null, null);
        log.info("Отправлен ответ для PATH запроса /items/{} с телом: {}", itemId, itemResponseDto);
        return itemResponseDto;
    }

    @GetMapping("/{itemId}")
    public ItemDTO itemGetDTO(@PathVariable Long itemId,
                           @RequestHeader ("X-Sharer-User-Id") Long userId)
    {
        log.info("Пришел GET запрос /items/{}", itemId);
        Item item = service.get(itemId, userId);
        ItemDTO itemResponseDto = ItemMapper.toItemDTO(item, item.getLastBooking(), item.getNextBooking(), item.getComments());
        log.info("Отправлен ответ для GET запроса /items/{} с телом: {}", itemId, itemResponseDto);
        return itemResponseDto;
    }

    @GetMapping
    public List<ItemDTO> itemsGetAllDTO(@RequestHeader("X-Sharer-User-Id") Long userId) {
        log.info("Пришел GET запрос /items");
        List<Item> itemDto = service.getAll(userId);
        List<ItemDTO> itemResponseDto = itemDto.stream()
                .map(o1 -> ItemMapper.toItemDTO(o1, o1.getLastBooking(), o1.getNextBooking(), o1.getComments()))
                .collect(Collectors.toList());
        log.info("Отправлен ответ для GET запроса /items с телом: {}", itemResponseDto);
        return itemResponseDto;
    }

    @GetMapping("/search")
    public List<ItemDTO> itemsGetAllSearchDTO(@RequestParam String text) {
        log.info("Пришел GET запрос /items/search?text={}", text);
        List<Item> itemDto = service.getAllSearch(text);
        List<ItemDTO> itemResponseDto = itemDto.stream()
                .map(o1 -> ItemMapper.toItemDTO(o1, o1.getLastBooking(), o1.getNextBooking(), o1.getComments()))
                .collect(Collectors.toList());
        log.info("Отправлен ответ для GET запроса /items/search?text={} с телом: {}", text, itemResponseDto);
        return itemResponseDto;
    }

    @PostMapping("/{itemId}/comment")
    public CommentsDTO addCommentsDTO(
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
