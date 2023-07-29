package ru.practicum.booking;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.item.Item;
import ru.practicum.user.User;

import java.util.List;
import java.util.Optional;

public interface BookingRepositoryJpa extends JpaRepository<Booking, Long> {
    List<Booking> findAllByBooker_Id(Long bookerId);
    List<Booking> findAllByItem_Owner_Id(Long ownerId);
    Optional<Booking> findByBooker_IdAndItem_Id(Long booker, Long item);
}
