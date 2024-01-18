package ru.practicum.shareit.unitServiceTests;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.practicum.shareit.booking.dto.BookingRequestDto;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.service.BookingService;
import ru.practicum.shareit.booking.status.Status;
import ru.practicum.shareit.booking.storage.BookingStorage;
import ru.practicum.shareit.item.coments.model.Comments;
import ru.practicum.shareit.item.dto.BookingToItem;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.storage.ItemStorage;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.request.model.ItemRequestToItem;
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
public class BookingServiceTests {
    @Mock
    private UserStorage userStorage;
    @Mock
    private ItemStorage itemStorage;
    @Mock
    private BookingStorage bookingStorage;
    @InjectMocks
    private BookingService bookingService;
    private User user;
    private Item item;
    private ItemRequest itemRequest;
    private BookingRequestDto requestDto;
    private Booking booking;


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

        requestDto = new BookingRequestDto(
                1L,
                LocalDateTime.now().plusHours(1),
                LocalDateTime.now().plusDays(1)
        );

        booking = new Booking(
                requestDto.getItemId(),
                requestDto.getStart(),
                requestDto.getEnd(),
                item,
                user,
                Status.WAITING

        );


    }

    @Test
    public void createBooking() {
        when(itemStorage.findById(anyLong()))
                .thenReturn(Optional.of(item));
        when(userStorage.findById(anyLong()))
                .thenReturn(Optional.of(user));
        when(bookingStorage.save(any()))
                .thenReturn(booking);
        Booking booking = bookingService.create(requestDto, 2L);
        assertEquals(this.booking, booking);
    }

    @Test
    public void approvedBooking() {
        when(userStorage.findById(anyLong()))
                .thenReturn(Optional.of(user));
        when(bookingStorage.findById(anyLong()))
                .thenReturn(Optional.of(booking));
        when(bookingStorage.save(any()))
                .thenReturn(booking);
        Booking booking = bookingService.approved(this.booking.getId(), user.getId(), false);
        assertEquals(this.booking, booking);
    }

    @Test
    public void getBookingId() {
        when(bookingStorage.findById(anyLong()))
                .thenReturn(Optional.of(booking));
        Booking booking = bookingService.get(this.booking.getId(), user.getId());
        assertEquals(this.booking, booking);
    }

    @Test
    public void getAll() {
        when(bookingStorage
                .findByBooker_IdOrderByStartTimeDesc(anyLong()))
                .thenReturn(List.of(booking));
        List<Booking> booking = bookingService.getAll(this.booking.getId(), "ALL");
        assertEquals(List.of(this.booking), booking);
    }

    @Test
    public void getAllOwnerId() {
        when(bookingStorage
                .findByItem_OwnerIdOrderByStartTimeDesc(anyLong()))
                .thenReturn(List.of(booking));
        when(userStorage.findById(anyLong()))
                .thenReturn(Optional.of(user));
        List<Booking> booking = bookingService.getAllOwnerId(this.booking.getId(), "ALL");
        assertEquals(List.of(this.booking), booking);
    }

    @Test
    public void getAllBookerIdFromAndSize() {
        when(userStorage.findById(anyLong()))
                .thenReturn(Optional.of(user));
        when(bookingStorage
                .findByBooker_IdOrderByStartTimeDesc(anyLong(), any()))
                .thenReturn(List.of(booking));
        List<Booking> booking = bookingService.getAllBookerIdFromAndSize(this.booking.getId(), 1, 1);
        assertEquals(List.of(this.booking), booking);
    }

    @Test
    public void getAllOwnerIdFromAndSize() {
        when(userStorage.findById(anyLong()))
                .thenReturn(Optional.of(user));
        when(bookingStorage
                .findByItem_OwnerIdOrderByStartTimeDesc(anyLong(), any()))
                .thenReturn(List.of(booking));
        List<Booking> booking = bookingService.getAllOwnerIdFromAndSize(this.booking.getId(), 1, 1);
        assertEquals(List.of(this.booking), booking);
    }


}
