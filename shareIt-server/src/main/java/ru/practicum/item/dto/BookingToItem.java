package ru.practicum.item.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class BookingToItem {
    private Long id;
    private Long BookerId;
}
