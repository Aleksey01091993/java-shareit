package ru.practicum.shareit.item.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import ru.practicum.shareit.item.coments.DTO.CommentsDTO;

import java.util.List;


@Data
@AllArgsConstructor
public class ItemResponseDto {
    private Long id;
    private String name;
    private String description;
    private Boolean available;
    private BookingToItem lastBooking;
    private BookingToItem nextBooking;
    private Long requestId;
    private List<CommentsDTO> comments;
}
