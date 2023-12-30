package ru.practicum.shareit.item.mapper;

import ru.practicum.shareit.booking.dto.BookingToItem;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.item.coments.model.Comments;
import ru.practicum.shareit.item.dto.ItemBookerDTO;
import ru.practicum.shareit.item.dto.ItemDTO;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;

import java.util.List;

public class ItemMapper {
    public static ItemDTO toItemDto(Item item) {
        return new ItemDTO(
                item.getId(),
                item.getName(),
                item.getDescription(),
                item.getAvailable(),
                item.getRequest() != null ? item.getRequest().getId() : null
        );
    }

    public static Item toItem(ItemDTO itemDTO, User user) {
        Item newItem = new Item();
        newItem.setName(itemDTO.getName());
        newItem.setDescription(itemDTO.getDescription());
        newItem.setAvailable(itemDTO.getAvailable());
        newItem.setOwner(user);
        return newItem;
    }

    public static ItemBookerDTO toItemBookerDTO(Item item, Booking lastBooking, Booking nextBooking, List<Comments> comments) {
        return new ItemBookerDTO(
                item.getId(),
                item.getName(),
                item.getDescription(),
                item.getAvailable(),
                item.getRequest() != null ? item.getRequest().getId() : null,
                lastBooking == null ? null : new BookingToItem(lastBooking.getId(), lastBooking.getBooker().getId()),
                nextBooking == null ? null : new BookingToItem(nextBooking.getId(), nextBooking.getBooker().getId()),
                comments
        );
    }

}
