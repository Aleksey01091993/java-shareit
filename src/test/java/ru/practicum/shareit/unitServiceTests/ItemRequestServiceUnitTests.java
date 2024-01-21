package ru.practicum.shareit.unitServiceTests;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.practicum.shareit.item.coments.model.Comments;
import ru.practicum.shareit.item.dto.BookingToItem;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.storage.ItemStorage;
import ru.practicum.shareit.request.dto.ItemRequestCreateRequestDto;
import ru.practicum.shareit.request.dto.ItemRequestResponseDto;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.request.model.ItemRequestToItem;
import ru.practicum.shareit.request.service.ItemRequestService;
import ru.practicum.shareit.request.storage.ItemRequestStorage;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.storage.UserStorage;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class ItemRequestServiceUnitTests {
    @Mock
    private ItemRequestStorage itemRequestStorage;
    @Mock
    private UserStorage userStorage;

    @Mock
    private ItemStorage itemStorage;

    @InjectMocks
    private ItemRequestService itemRequestService;

    private ItemRequest itemRequest;
    private User user;
    private ItemRequestResponseDto responseDto;
    private ItemRequestCreateRequestDto itemRequestCreateRequestDto;
    private Item item;

    @BeforeEach
    public void setup() {
        itemRequestCreateRequestDto = new ItemRequestCreateRequestDto("text");
        user = new User(1L, "name", "email@email");


        responseDto = new ItemRequestResponseDto(
                1L,
                "text",
                LocalDateTime.now(),
                List.of(new ItemRequestToItem(
                        1L,
                        "name",
                        "text",
                        true,
                        1L
                ))
        );

        itemRequest = new ItemRequest(
                1L,
                "text",
                user,
                responseDto.getCreated(),
                List.of(new ItemRequestToItem(
                        1L,
                        "name",
                        "text",
                        true,
                        1L
                ))
        );

        item = new Item(
                1L,
                "name",
                "text",
                true,
                new BookingToItem(1L, 2L),
                new BookingToItem(3L, 4L),
                user,
                itemRequest,
                List.of(new Comments(
                        1L,
                        "text",
                        1L,
                        user,
                        LocalDateTime.now()
                ))

        );
        when(userStorage.findById(anyLong()))
                .thenReturn(Optional.of(user));

    }

    @Test
    public void createItemRequest() {
        when(itemRequestStorage.save(any()))
                .thenReturn(itemRequest);
        ItemRequest itemRequest = itemRequestService.create(itemRequestCreateRequestDto, user.getId());
        assertEquals(this.itemRequest, itemRequest);
    }

    @Test
    public void findByAll() {
        when(itemRequestStorage.findByRequestor_IdOrderByCreatedAsc(anyLong()))
                .thenReturn(List.of(itemRequest));
        List<ItemRequest> itemRequests = itemRequestService.findByAll(1L);
        assertEquals(itemRequests, List.of(this.itemRequest));
    }

    @Test
    public void findById() {
        when(itemRequestStorage.findById(anyLong()))
                .thenReturn(Optional.of(itemRequest));
        ItemRequest itemRequest = itemRequestService.findById(1L, 1L);
        assertEquals(itemRequest, this.itemRequest);
    }

    @Test
    public void getAllFrom() {
        when(itemRequestStorage.findByRequestor_IdNotOrderByCreatedAsc(anyLong(), any()))
                .thenReturn(List.of(itemRequest));
        List<ItemRequest> itemRequests = itemRequestService.getAllFrom(1L, 1, 1);
        assertEquals(itemRequests, List.of(this.itemRequest));
    }

}
