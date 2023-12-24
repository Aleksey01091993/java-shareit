package ru.practicum.shareit.booking.mapper;

import ru.practicum.shareit.booking.dto.BookingCreationDTO;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;

public class BookingMapper {
    public static Booking toBooking(BookingCreationDTO bookingCreationDTO, Item item, User user) {
        return new Booking(
                bookingCreationDTO.getId(),
                bookingCreationDTO.getStart(),
                bookingCreationDTO.getEnd(),
                item,
                user,
                bookingCreationDTO.getStatus()
                );
    }

    public static BookingDto toBookingDTO(Booking booking) {
        Item item = new Item();
        item.setId(booking.getItem().getId());
        item.setName(booking.getItem().getName());
        User user = new User();
        user.setId(booking.getBooker().getId());
        return new BookingDto(
                booking.getId(),
                booking.getStartTime(),
                booking.getEndTime(),
                booking.getStatus(),
                user,
                item
                );
    }
}
