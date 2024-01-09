package ru.practicum.shareit.booking.mapper;

import ru.practicum.shareit.booking.dto.BookerToBooking;
import ru.practicum.shareit.booking.dto.BookingResponseDTO;
import ru.practicum.shareit.booking.dto.ItemToBooking;
import ru.practicum.shareit.booking.model.Booking;

public class BookingMapper {

    public static BookingResponseDTO toBookingDto(Booking booking) {
        return new BookingResponseDTO(
                booking.getId(),
                booking.getStartTime(),
                booking.getEndTime(),
                booking.getStatus(),
                new BookerToBooking(booking.getBooker().getId()),
                new ItemToBooking(booking.getItem().getId(), booking.getItem().getName()));
    }


}
