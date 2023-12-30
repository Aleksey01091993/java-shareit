package ru.practicum.shareit.item.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import ru.practicum.shareit.booking.dto.BookingToItem;
import ru.practicum.shareit.item.coments.model.Comments;
import ru.practicum.shareit.item.model.Item;

import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
public class ItemBookerDTO extends Item {
    private Long id;
    private String name;
    private String description;
    private Boolean available;
    private Long requestId;
    private BookingToItem lastBooking;
    private BookingToItem nextBooking;
    private List<Comments> comments;


}
