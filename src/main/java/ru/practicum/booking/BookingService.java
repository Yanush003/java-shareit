package ru.practicum.booking;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.item.Item;
import ru.practicum.item.ItemDto;

import java.util.List;

@Service
@RequiredArgsConstructor
public class BookingService {
    private final BookingRepository bookingRepository;

    public BookingDto create(Long userId, ItemDto itemDto) {
        return null;
    }

    public Item update(Long userId, Long bookingId, ItemDto itemDto, Boolean approved) {
        return null;
    }

    public Item get(Long bookingId) {
        return null;
    }

    public List<Booking> getAllBooking(Long userId, String state) {
        return null;
    }

    public List<Booking> getOwnerBooking(Long userId, String state) {
        return null;
    }
}
