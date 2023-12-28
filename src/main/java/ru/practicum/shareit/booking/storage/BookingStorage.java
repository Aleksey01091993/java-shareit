package ru.practicum.shareit.booking.storage;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.status.Status;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

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
    Optional<Booking> findFirstByEndTimeBeforeAndItemIdAndItem_OwnerIdOrderByEndTime(LocalDateTime dateTime, Long itemId, Long ownerId);
    Optional<Booking> findFirstByStartTimeAfterAndItemIdAndItem_OwnerIdOrderByStartTime(LocalDateTime dateTime, Long itemId, Long ownerId);
    Optional<Booking> findFirstByBookerIdAndItemIdAndStatusAndEndTimeBefore(Long userId, Long itemId, Status status, LocalDateTime dateTime);



}
