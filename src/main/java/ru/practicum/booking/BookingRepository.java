package ru.practicum.booking;

import ru.practicum.item.Item;
import ru.practicum.item.ItemDto;

import java.util.List;

public interface BookingRepository {
    BookingDto create(Long userId, ItemDto itemDto);

    Item update(Long userId, Long bookingId, ItemDto itemDto, Boolean approved);

    Item get(Long bookingId);

    List<Booking> getAllBooking(Long userId, String state);

    List<Booking> getOwnerBooking(Long userId, String state);
}
