package ru.practicum.shareit.booking.service;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.booking.dto.BookingCreationDTO;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.storage.BookingStorage;
import ru.practicum.shareit.exception.BadRequest400;
import ru.practicum.shareit.item.service.ItemService;
import ru.practicum.shareit.user.service.UserService;

import static ru.practicum.shareit.booking.status.Status.WAITING;

@Service
public class BookingService {
    private final UserService userService;
    private final ItemService itemService;
    private final BookingStorage bookingStorage;

    public BookingService(
            @Autowired UserService userService,
            @Autowired ItemService itemService,
            @Autowired BookingStorage bookingStorage
    ) {
        this.userService = userService;
        this.itemService = itemService;
        this.bookingStorage = bookingStorage;
    }

    public Booking create(BookingCreationDTO bookingCreationDTO, Long userId) {
        if (bookingCreationDTO.getStart().isAfter(bookingCreationDTO.getEnd()) ||
                bookingCreationDTO.getStart().equals(bookingCreationDTO.getEnd()) ||
                !(itemService.get(bookingCreationDTO.getItemId()).getAvailable())) {
            throw new BadRequest400("дата окончания позже даты начала");
        }
        Booking booking = new Booking(
                bookingCreationDTO.getId(),
                bookingCreationDTO.getStart(),
                bookingCreationDTO.getEnd(),
                itemService.get(bookingCreationDTO.getItemId()),
                userService.get(userId),
                WAITING
        );
        return bookingStorage.save(booking);


    }


}
