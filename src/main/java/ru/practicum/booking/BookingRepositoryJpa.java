package ru.practicum.booking;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BookingRepositoryJpa extends JpaRepository<Booking, Long> {
    List<Booking> findAllByBooker_Id(Long bookerId);
    List<Booking> findAllByItem_Owner_Id(Long ownerId);
}
