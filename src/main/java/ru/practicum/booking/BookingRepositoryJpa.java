package ru.practicum.booking;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.request.ItemRequest;
import ru.practicum.user.User;

import java.util.List;

public interface BookingRepositoryJpa extends JpaRepository<Booking, Long> {

    List<Booking> findAllByBookerId(Long userId);

    Page<Booking> findAllByBookerId(Long userId, Pageable pageable);

    List<Booking> findAllByItem_OwnerId(Long itemOwner);

    Page<Booking> findAllByItem_OwnerId(Long itemOwner, Pageable pageable);

    List<Booking> findAllByItemIdAndBookerId(Long itemId, Long bookerId);

    List<Booking> findAllByItemId(Long itemId);

    Page<Booking> findAllByItemId(Long itemId, Pageable pageable);
}
