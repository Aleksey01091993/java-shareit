package ru.practicum.shareit.item.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import ru.practicum.shareit.booking.dto.BookerToItem;
import ru.practicum.shareit.item.model.Item;

@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
public class ItemBookerDTO extends Item {
    private Long id;
    private String name;
    private String description;
    private Boolean available;
    private Long requestId;
    private BookerToItem lastBooking;
    private BookerToItem nextBooking;


}
