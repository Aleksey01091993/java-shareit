package ru.practicum.shareit.item.mapper;

import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.item.coments.mapper.CommentMapper;
import ru.practicum.shareit.item.coments.model.Comments;
import ru.practicum.shareit.item.dto.BookingToItem;
import ru.practicum.shareit.item.dto.ItemResponseDto;
import ru.practicum.shareit.item.dto.ItemCreateRequestDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;

import java.util.List;
import java.util.stream.Collectors;

public class ItemMapper {
    public static ItemCreateRequestDto toItemResponseDto(Item item) {
        return new ItemCreateRequestDto(
                item.getName(),
                item.getDescription(),
                item.getAvailable()
        );
    }

    public static Item toItem(Item item,
                              ItemCreateRequestDto itemCreateRequestDto,
                              User user) {
        return new Item(
                item == null ? null : item.getId(),
                itemCreateRequestDto.getName() == null ?  item.getName() : itemCreateRequestDto.getName(),
                itemCreateRequestDto.getDescription() == null ? item.getDescription() : itemCreateRequestDto.getDescription(),
                itemCreateRequestDto.getAvailable() == null ? item.getAvailable() : itemCreateRequestDto.getAvailable(),
                item == null ? null : item.getLastBooking(),
                item == null ? null : item.getNextBooking(),
                user,
                item == null ? null : item.getComments()
                );
    }

    public static ItemResponseDto toItemDTO(Item item, List<Comments> comments) {
        return new ItemResponseDto(
                item.getId(),
                item.getName(),
                item.getDescription(),
                item.getAvailable(),
                item.getLastBooking(),
                item.getNextBooking(),
                comments == null ? null : comments.stream().map(CommentMapper::toCommentsDTO).collect(Collectors.toList())
        );
    }
    public static BookingToItem toBookingToItem(Booking bookingToItem) {
        if (bookingToItem == null) {
            return null;
        } else {
            return new BookingToItem(bookingToItem.getId(), bookingToItem.getBooker().getId());
        }
    }

}
