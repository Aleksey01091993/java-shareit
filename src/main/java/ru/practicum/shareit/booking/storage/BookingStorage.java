package ru.practicum.shareit.booking.storage;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.RequestParam;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.status.Status;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface BookingStorage extends JpaRepository<Booking, Long> {
    List<Booking> findByBooker_IdOrderByStartTimeDesc(Long id);
    List<Booking> findByBooker_IdAndStatusOrderByStartTimeDesc(Long id, Status status);
    List<Booking> findByItem_OwnerIdOrderByStartTimeDesc(Long id);
    List<Booking> findByItem_OwnerIdAndStatusOrderByStartTimeDesc(Long id, Status status);
    List<Booking> findByBooker_IdAndStartTimeLessThanAndEndTimeGreaterThanOrderByStartTimeDesc(Long id, LocalDateTime dateTime, LocalDateTime dateTimeOne);
    List<Booking> findByBooker_IdAndEndTimeGreaterThanOrderByStartTimeDesc(Long id, LocalDateTime dateTime);
    List<Booking> findByBooker_IdAndStartTimeGreaterThanOrderByStartTimeDesc(Long id, LocalDateTime dateTime);
    List<Booking> findByItem_OwnerIdAndStartTimeLessThanAndEndTimeGreaterThanOrderByStartTimeDesc(Long id, LocalDateTime dateTime, LocalDateTime dateTimeOne);
    List<Booking> findByItem_OwnerIdAndEndTimeGreaterThanOrderByStartTimeDesc(Long id, LocalDateTime dateTime);
    List<Booking> findByItem_OwnerIdAndStartTimeGreaterThanOrderByStartTimeDesc(Long id, LocalDateTime dateTime);
    List<Booking> findByItem_IdAndItem_OwnerIdAndEndTimeBeforeOrderByEndTimeAsc(Long itemId, Long ownerId, LocalDateTime dateTime);
    List<Booking> findByItem_IdAndItem_OwnerIdAndStartTimeAfterOrderByEndTimeAsc(Long itemId, Long ownerId, LocalDateTime dateTime);


}
