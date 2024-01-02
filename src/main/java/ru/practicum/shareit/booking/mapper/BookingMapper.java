package ru.practicum.shareit.booking.mapper;

import ru.practicum.shareit.booking.dto.BookingDTO;
import ru.practicum.shareit.booking.dto.BookerToBooking;
import ru.practicum.shareit.booking.dto.ItemToBooking;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;

public class BookingMapper {

    public static BookingDTO toBookingDto(Booking booking) {
        return new BookingDTO(
                booking.getId(),
                booking.getStartTime(),
                booking.getEndTime(),
                booking.getStatus(),
                new BookerToBooking(booking.getBooker().getId()),
                new ItemToBooking(booking.getItem().getId(), booking.getItem().getName()));
    }


}
