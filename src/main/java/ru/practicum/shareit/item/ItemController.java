package ru.practicum.shareit.item;

import jakarta.annotation.Nullable;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.exception.NotFound404;
import ru.practicum.shareit.item.dto.ItemDTO;
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
    public Item itemCreateRequestDTO(@RequestBody @Valid ItemDTO item,
                                     @RequestHeader("X-Sharer-User-Id") Long userId) throws NotFound404 {
        log.info("Пришел POST запрос /items с телом: {}", item);
        ItemDTO itemResponseDto = ItemMapper.toItemDto(service.create(item, userId));
        log.info("Отправлен ответ для POST запроса /items с телом: {}", itemResponseDto);
        return itemResponseDto;
    }

    @PatchMapping("/{itemId}")
    public Item itemUpdateDTO(@RequestBody @Nullable ItemDTO item,
                              @RequestHeader("X-Sharer-User-Id") Long userId,
                              @PathVariable Long itemId) {
        log.info("Пришел PATH запрос /items/{} с телом: {}", itemId, item);
        ItemDTO itemResponseDto = ItemMapper.toItemDto(service.update(item, userId, itemId));
        log.info("Отправлен ответ для PATH запроса /items/{} с телом: {}", itemId, itemResponseDto);
        return itemResponseDto;
    }

    @GetMapping("/{itemId}")
    public Item itemGetDTO(@PathVariable Long itemId) {
        log.info("Пришел GET запрос /items/{}", itemId);
        ItemDTO itemResponseDto = ItemMapper.toItemDto(service.get(itemId));
        log.info("Отправлен ответ для GET запроса /items/{} с телом: {}", itemId, itemResponseDto);
        return itemResponseDto;
    }

    @GetMapping
    public List<Item> itemsGetAllDTO(@RequestHeader("X-Sharer-User-Id") Long userId,
                                     @RequestParam(required = false) String text) {
        log.info("Пришел GET запрос /items");
        List<Item> itemResponseDto = service.getAll(userId).stream()
                .map(ItemMapper::toItemDto)
                .collect(Collectors.toList());
        log.info("Отправлен ответ для GET запроса /items с телом: {}", itemResponseDto);
        return itemResponseDto;
    }

    @GetMapping("/search")
    public List<Item> itemsGetAllSearchDTO(@RequestParam String search) {
        log.info("Пришел GET запрос /items/search?text={}", search);
        List<Item> itemDTOList = service.getAllSearch(search).stream()
                .map(ItemMapper::toItemDto)
                .collect(Collectors.toList());
        log.info("Отправлен ответ для GET запроса /items/search?text={} с телом: {}", search, itemDTOList);
        return itemDTOList;
    }


}
