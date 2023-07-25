package ru.practicum.booking;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.practicum.item.Item;
import ru.practicum.item.ItemDto;

import java.util.List;

@RestController
@RequestMapping("/bookings")
@RequiredArgsConstructor
public class BookingController {
    BookingService bookingService;

    @PostMapping
    public BookingDto create(@RequestHeader("X-Sharer-User-Id") Long userId,
                             @RequestBody ItemDto itemDto) {
        return null;
    }

    @PatchMapping("/{bookingId}?approved={approved}")
    public Item update(@RequestHeader("X-Sharer-User-Id") Long userId,
                       @PathVariable("bookingId") Long bookingId,
                       @RequestBody ItemDto itemDto, @PathVariable Boolean approved) {
        return null;
    }

    @GetMapping("/{bookingId}")
    public Item get(@PathVariable("bookingId") Long bookingId) {
        return null;
    }

    @GetMapping("?state={state}")
    public List<Booking> getAllBooking(@RequestHeader("X-Sharer-User-Id") Long userId, @PathVariable String state) {
        return null;
    }

    @GetMapping("owner?state={state}")
    public List<Booking> getOwnerBooking(@RequestHeader("X-Sharer-User-Id") Long userId, @PathVariable String state) {
        return null;
    }
}
