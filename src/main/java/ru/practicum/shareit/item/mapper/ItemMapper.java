package ru.practicum.shareit.item.mapper;

import ru.practicum.shareit.item.coments.DTO.CommentsDTO;
import ru.practicum.shareit.item.coments.mapper.CommentMapper;
import ru.practicum.shareit.item.dto.BookingToItem;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.item.coments.model.Comments;
import ru.practicum.shareit.item.dto.ItemDTO;
import ru.practicum.shareit.item.dto.ItemResponseDTO;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;

import java.util.List;
import java.util.stream.Collectors;

public class ItemMapper {
    public static ItemResponseDTO toItemResponseDto(Item item) {
        return new ItemResponseDTO(
                item.getName(),
                item.getDescription(),
                item.getAvailable()
        );
    }

    public static Item toItem(Long itemId,
                              ItemResponseDTO itemResponseDTO,
                              Booking lastBooking,
                              Booking nextBooking,
                              User user,
                              List<Comments> comments) {
        return new Item(itemId,
                itemResponseDTO.getName(),
                itemResponseDTO.getDescription(),
                itemResponseDTO.getAvailable(),
                lastBooking == null ? null : new BookingToItem(lastBooking.getId(), lastBooking.getBooker().getId()),
                nextBooking == null ? null : new BookingToItem(nextBooking.getId(), nextBooking.getBooker().getId()),
                user,
                comments
                );
    }

    public static ItemDTO toItemDTO(Item item, BookingToItem lastBooking, BookingToItem nextBooking, List<Comments> comments) {
        return new ItemDTO(
                item.getId(),
                item.getName(),
                item.getDescription(),
                item.getAvailable(),
                lastBooking,
                nextBooking,
                comments == null ? null : comments.stream().map(CommentMapper::toCommentsDTO).collect(Collectors.toList())
        );
    }

}
