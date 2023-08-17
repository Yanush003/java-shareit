package ru.practicum.booking;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BookingRepositoryJpa extends JpaRepository<Booking, Long> {
    Page<Booking> findAllByBookerId(Long userId, Pageable pageable);

    List<Booking> findAllByBooker_Id(Long bookerId);

    Page<Booking> findAllByItem_OwnerId(Long itemOwner, Pageable pageable);

    List<Booking> findAllByItemIdAndBookerId(Long itemId, Long bookerId);

    List<Booking> findAllByItemId(Long itemId);
}
