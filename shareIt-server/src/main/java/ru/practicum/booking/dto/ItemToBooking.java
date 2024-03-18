package ru.practicum.booking.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ItemToBooking {
    private Long id;
    private String name;
}
