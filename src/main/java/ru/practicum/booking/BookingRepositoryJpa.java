package ru.practicum.booking;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.item.Item;
import ru.practicum.user.User;

import java.util.List;
import java.util.Optional;

public interface BookingRepositoryJpa extends JpaRepository<Booking, Long> {
    @Query("select b from Booking b " +
            "where b.booker.id = :bookerId " +
            "and (:status is null or b.status = :status)  " +
            "order by b.start")
    List<Booking> findAllByBooker_IdOrderByStart(Long bookerId, String status);
    List<Booking> findAllByBooker_Id(Long id);
    List<Booking> findAllByItem_Owner_Id(Long ownerId);
    Optional<Booking> findByBooker_IdAndItem_Id(Long booker, Long item);
    @Query("select b from Booking b " +
            "where b.item.owner.id = :ownerId " +
            "and (:status is null or b.status = :status)  " +
            "order by b.start")
    List<Booking> findAllByOwner_IdOrderByStart(Long ownerId, String status);
}
