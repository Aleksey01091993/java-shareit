package ru.practicum.shareit.item.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import ru.practicum.shareit.item.coments.DTO.CommentsDTO;

import java.util.List;


@Data
@AllArgsConstructor
public class ItemDTO {
    private Long id;
    private String name;
    private String description;
    private Boolean available;
    private BookingToItem lastBooking;
    private BookingToItem nextBooking;
    private List<CommentsDTO> comments;


}
