package ru.practicum.booking.mapper;


import ru.practicum.booking.dto.BookerToBooking;
import ru.practicum.booking.dto.BookingResponseDTO;
import ru.practicum.booking.dto.ItemToBooking;
import ru.practicum.booking.model.Booking;

import static ru.practicum.booking.status.Status.APPROVED;

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

    public static BookingResponseDTO toBookingDtoCurrent(Booking booking) {
        if (booking.getStatus() == APPROVED) {
            return toBookingDto(booking);
        } else {
            return BookingResponseDTO.builder()
                    .id(booking.getId())
                    .status(booking.getStatus())
                    .booker(new BookerToBooking(booking.getBooker().getId()))
                    .item(new ItemToBooking(booking.getItem().getId(), booking.getItem().getName()))
                    .build();
        }

    }

    public static BookingResponseDTO toBookingDtoPast(Booking booking) {
        return BookingResponseDTO.builder()
                .id(booking.getId())
                .status(booking.getStatus())
                .booker(new BookerToBooking(booking.getBooker().getId()))
                .item(new ItemToBooking(booking.getItem().getId(), booking.getItem().getName()))
                .build();


    }

}
