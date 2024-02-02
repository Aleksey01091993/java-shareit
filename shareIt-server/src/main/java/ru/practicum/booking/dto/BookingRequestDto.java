package ru.practicum.booking.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;

@AllArgsConstructor
@Data
public class BookingRequestDto {
    private Long itemId;
    private LocalDateTime start;
    private LocalDateTime end;
}
