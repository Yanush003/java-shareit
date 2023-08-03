package ru.practicum.booking;

import ru.practicum.item.Item;
import ru.practicum.item.ItemDto;

public interface BookingRepository {
    BookingDto add(Long userId, ItemDto itemDto);

    Item update(Long userId, Long bookingId, ItemDto itemDto, Boolean approved);

    Item get(Long bookingId);
}
