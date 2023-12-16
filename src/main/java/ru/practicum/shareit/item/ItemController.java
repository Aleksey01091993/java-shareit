package ru.practicum.shareit.item;

import jakarta.annotation.Nullable;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.item.dto.ItemDTO;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.service.ItemService;

import java.util.List;

/**
 * TODO Sprint add-controllers.
 */
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
    public ItemDTO create(@RequestBody Item item, @RequestHeader("X-Sharer-User-Id") Long userId) throws Exception {
        log.info("Пришел POST запрос /items с телом: {}", item);
        ItemDTO itemDTO = service.create(item, userId);
        log.info("Отправлен ответ для POST запроса /items с телом: {}", itemDTO);
        return itemDTO;
    }

    @PatchMapping("/{itemId}")
    public ItemDTO update(@RequestBody @Nullable Item item,
                          @RequestHeader("X-Sharer-User-Id") Long userId,
                          @PathVariable Long itemId) throws Exception {
        log.info("Пришел PATH запрос /items/{} с телом: {}", itemId, item);
        ItemDTO itemDTO = service.update(item, userId, itemId);
        log.info("Отправлен ответ для PATH запроса /items/{} с телом: {}", itemDTO, itemDTO);
        return itemDTO;
    }

    @GetMapping("/{itemId}")
    public ItemDTO get(@PathVariable Long itemId) throws Exception {
        log.info("Пришел GET запрос /items/{}", itemId);
        ItemDTO itemDTO = service.get(itemId);
        log.info("Отправлен ответ для GET запроса /items/{} с телом: {}", itemId, itemDTO);
        return itemDTO;
    }

    @GetMapping
    public List<ItemDTO> getAll(@RequestHeader("X-Sharer-User-Id") Long userId) {
        log.info("Пришел GET запрос /items");
        List<ItemDTO> itemDTOList = service.getAll(userId);
        log.info("Отправлен ответ для GET запроса /items с телом: {}", itemDTOList);
        return itemDTOList;
    }


}
