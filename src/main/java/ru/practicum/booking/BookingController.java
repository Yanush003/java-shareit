package ru.practicum.booking;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/bookings")
@RequiredArgsConstructor
public class BookingController {
    private final BookingService bookingService;

    @PostMapping
    public BookingDto add(@RequestHeader("X-Sharer-User-Id") Long userId,
                          @RequestBody @Valid BookingPostDto bookingDto) {
        return bookingService.add(userId, bookingDto);
    }

    @PatchMapping("/{bookingId}")
    public Booking update(@RequestHeader("X-Sharer-User-Id") Long userId,
                          @PathVariable("bookingId") Long bookingId,
                          @RequestParam Boolean approved) {
        return bookingService.update(userId, bookingId, approved);
    }

    @GetMapping("/{bookingId}")
    public Booking get(@PathVariable("bookingId") Long bookingId, @RequestHeader("X-Sharer-User-Id") Long userId) {
        return bookingService.getByUserId(userId, bookingId);
    }

    @GetMapping
    public List<Booking> getAllBooking(@RequestHeader("X-Sharer-User-Id") Long userId,
                                       @RequestParam(required = false) String state) {
        return bookingService.getAllBooking(userId, state);
    }

    @GetMapping("owner")
    public List<Booking> getOwnerBooking(@RequestHeader("X-Sharer-User-Id") Long userId,
                                         @RequestParam(required = false) String state) {
        return bookingService.getOwnerBooking(userId, state);
    }
}
