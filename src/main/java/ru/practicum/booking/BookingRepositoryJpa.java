package ru.practicum.booking;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BookingRepositoryJpa extends JpaRepository<Booking, Long> {

    List<Booking> findAllByBookerId(long userId);

    List<Booking> findAllByItem_OwnerId(long itemOwner);

    List<Booking> findAllByItemIdAndBookerId(long itemId, long bookerId);

    List<Booking> findAllByItemId(Long itemId);
}
