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
    private final BookingService bookingService;

    @PostMapping
    public BookingDto add(@RequestHeader("X-Sharer-User-Id") Long userId,
                          @RequestBody BookingPostDto bookingDto) {
        return bookingService.add(userId, bookingDto);
    }

    @PatchMapping("/{bookingId}?approved={approved}")
    public Item update(@RequestHeader("X-Sharer-User-Id") Long userId,
                       @PathVariable("bookingId") Long bookingId,
                       @PathVariable Boolean approved) {
        return bookingService.update(userId, bookingId, approved);
    }

    @GetMapping("/{bookingId}")
    public Booking get(@PathVariable("bookingId") Long bookingId, @RequestHeader("X-Sharer-User-Id") Long userId) {
        return bookingService.getByUserId(userId, bookingId);
    }

    @GetMapping("?state={state}")
    public List<Booking> getAllBooking(@RequestHeader("X-Sharer-User-Id") Long userId, @PathVariable String state) {
        return bookingService.getAllBooking(userId, state);
    }

    @GetMapping("owner?state={state}")
    public List<Booking> getOwnerBooking(@RequestHeader("X-Sharer-User-Id") Long userId, @PathVariable String state) {
        return bookingService.getOwnerBooking(userId, state);
    }
}
