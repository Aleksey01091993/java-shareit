package ru.practicum.booking.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.practicum.booking.status.Status;

import java.time.LocalDateTime;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class BookingResponseDTO {
    private Long id;
    private LocalDateTime start;
    private LocalDateTime end;
    private Status status;
    private BookerToBooking booker;
    private ItemToBooking item;
}
