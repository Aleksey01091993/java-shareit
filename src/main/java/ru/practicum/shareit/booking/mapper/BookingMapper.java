package ru.practicum.shareit.booking.mapper;

import ru.practicum.shareit.booking.dto.BookingAndTimeDTO;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;

public class BookingMapper {

    public static BookingAndTimeDTO toBookingAndTimeDTO(Booking booking) {
        Item item = new Item();
        item.setId(booking.getItem().getId());
        item.setName(booking.getItem().getName());
        User user = new User();
        user.setId(booking.getBooker().getId());
        return new BookingAndTimeDTO(
                booking.getId(),
                booking.getStartTime(),
                booking.getEndTime(),
                booking.getStatus(),
                user,
                item
        );
    }


}
