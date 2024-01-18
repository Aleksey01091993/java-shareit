package ru.practicum.shareit.unitServiceTests;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.status.Status;
import ru.practicum.shareit.booking.storage.BookingStorage;
import ru.practicum.shareit.item.coments.DTO.CommentsDTO;
import ru.practicum.shareit.item.coments.model.Comments;
import ru.practicum.shareit.item.coments.storage.CommentsStorage;
import ru.practicum.shareit.item.dto.BookingToItem;
import ru.practicum.shareit.item.dto.ItemCreateRequestDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.service.ItemService;
import ru.practicum.shareit.item.storage.ItemStorage;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.request.model.ItemRequestToItem;
import ru.practicum.shareit.request.storage.ItemRequestStorage;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.storage.UserStorage;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class ItemServiceUnitTests {
    @Mock
    private UserStorage userStorage;
    @Mock
    private ItemStorage itemStorage;
    @Mock
    private BookingStorage bookingStorage;
    @Mock
    private ItemRequestStorage itemRequestStorage;
    @Mock
    private CommentsStorage commentsStorage;
    @InjectMocks
    private ItemService service;

    private User user;
    private Item item;
    private ItemRequest itemRequest;
    private Booking booking;
    private Comments comments;
    private CommentsDTO commentsDTO;

    ItemCreateRequestDto itemCreateRequestDto;

    @BeforeEach
    public void setup() {
        itemRequest = new ItemRequest(
                1L,
                "text",
                user,
                LocalDateTime.now(),
                List.of(new ItemRequestToItem(
                        1L,
                        "name",
                        "text",
                        true,
                        1L
                ))
        );

        itemCreateRequestDto = new ItemCreateRequestDto(
                "name",
                "text",
                true,
                itemRequest.getId()
        );

        user = new User(
                1L,
                "name",
                "email@email");

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


        booking = new Booking(
                1L,
                LocalDateTime.now(),
                LocalDateTime.now(),
                item,
                user,
                Status.WAITING

        );
        comments = new Comments(
                1L,
                "text",
                1L,
                user,
                LocalDateTime.now()
        );

        commentsDTO = new CommentsDTO(
                1L,
                "text",
                user.getName(),
                comments.getCreated()
        );


    }

    @Test
    public void create() {
        when(userStorage.findById(anyLong()))
                .thenReturn(Optional.of(user));
        when(itemRequestStorage.findById(anyLong()))
                .thenReturn(Optional.of(itemRequest));
        when(itemStorage.save(any()))
                .thenReturn(item);
        Item item = service.create(itemCreateRequestDto, 1L);
        assertEquals(this.item, item);

    }

    @Test
    public void update() {
        when(userStorage.findById(anyLong()))
                .thenReturn(Optional.of(user));
        when(itemRequestStorage.findById(anyLong()))
                .thenReturn(Optional.of(itemRequest));
        when(itemStorage.save(any()))
                .thenReturn(item);
        when(itemStorage.findById(anyLong()))
                .thenReturn(Optional.of(item));
        Item item = service.update(itemCreateRequestDto, 1L, 1L);
        assertEquals(this.item, item);
    }

    @Test
    public void get() {
        when(itemStorage.findById(any()))
                .thenReturn(Optional.of(item));
        when(bookingStorage
                .findFirstByEndTimeBeforeAndItemIdAndItem_OwnerIdOrderByEndTimeDesc(any(), anyLong(), anyLong())
        ).thenReturn(Optional.of(booking));
        when(bookingStorage
                .findFirstByStartTimeAfterAndItemIdAndItem_OwnerIdAndStatusOrderByStartTime(any(), anyLong(), anyLong(), any())
        ).thenReturn(Optional.of(booking));
        when(commentsStorage.findAllByItemId(anyLong())).thenReturn(List.of(comments));
        Item item = service.get(1L, 1L);
        assertEquals(this.item, item);
    }

    @Test
    public void getAll() {
        when(itemStorage.findAllByOwnerIdOrderById(any()))
                .thenReturn(List.of(item));
        when(bookingStorage
                .findFirstByEndTimeBeforeAndItemIdAndItem_OwnerIdOrderByEndTimeDesc(any(), anyLong(), anyLong())
        ).thenReturn(Optional.of(booking));
        when(bookingStorage
                .findFirstByStartTimeAfterAndItemIdAndItem_OwnerIdAndStatusOrderByStartTime(any(), anyLong(), anyLong(), any())
        ).thenReturn(Optional.of(booking));
        when(commentsStorage.findAllByItemId(anyLong())).thenReturn(List.of(comments));
        List<Item> item = service.getAll(1L);
        assertEquals(List.of(this.item), item);
    }

    @Test
    public void getAllFromAndSize() {
        when(itemStorage.findAllByOwnerIdOrderById(any(), any()))
                .thenReturn(List.of(item));
        when(bookingStorage
                .findFirstByEndTimeBeforeAndItemIdAndItem_OwnerIdOrderByEndTimeDesc(any(), anyLong(), anyLong())
        ).thenReturn(Optional.of(booking));
        when(bookingStorage
                .findFirstByStartTimeAfterAndItemIdAndItem_OwnerIdAndStatusOrderByStartTime(any(), anyLong(), anyLong(), any())
        ).thenReturn(Optional.of(booking));
        when(commentsStorage.findAllByItemId(anyLong())).thenReturn(List.of(comments));
        List<Item> item = service.getAllFromAndSize(1L, 1, 1);
        assertEquals(List.of(this.item), item);
    }

    @Test
    public void getAllSearch() {
        when(itemStorage
                .findAllByNameContainingIgnoreCaseOrDescriptionContainingIgnoreCaseAndAvailable(anyString(), anyString(), anyBoolean()))
                .thenReturn(List.of(item));
        when(bookingStorage
                .findFirstByEndTimeBeforeAndItemIdAndItem_OwnerIdOrderByEndTimeDesc(any(), anyLong(), anyLong())
        ).thenReturn(Optional.of(booking));
        when(bookingStorage
                .findFirstByStartTimeAfterAndItemIdAndItem_OwnerIdAndStatusOrderByStartTime(any(), anyLong(), anyLong(), any())
        ).thenReturn(Optional.of(booking));
        when(commentsStorage.findAllByItemId(anyLong())).thenReturn(List.of(comments));
        List<Item> item = service.getAllSearch(" 1L, 1, 1");
        assertEquals(List.of(this.item), item);
    }

    @Test
    public void addComment() {
        when(userStorage.findById(anyLong()))
                .thenReturn(Optional.of(user));
        when(itemStorage.findById(anyLong()))
                .thenReturn(Optional.of(item));
        when(bookingStorage.findFirstByBookerIdAndItemIdAndStatusAndEndTimeBefore(anyLong(), anyLong(), any(), any()))
                .thenReturn(Optional.of(booking));
        when(commentsStorage.save(any()))
                .thenReturn(comments);
        Comments comments = service.addComment(1L, 1L, commentsDTO);
        assertEquals(this.comments, comments);
    }


}
