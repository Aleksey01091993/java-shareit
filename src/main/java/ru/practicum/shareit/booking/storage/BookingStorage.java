package ru.practicum.shareit.booking.storage;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.RequestParam;
import ru.practicum.shareit.booking.model.Booking;

@Repository
public interface BookingStorage extends JpaRepository<Booking, Long> {
}
