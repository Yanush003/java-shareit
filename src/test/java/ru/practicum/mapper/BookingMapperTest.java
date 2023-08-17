package ru.practicum.mapper;

import org.junit.jupiter.api.Test;
import ru.practicum.booking.Booking;
import ru.practicum.booking.BookingDto;
import ru.practicum.booking.BookingMapper;
import ru.practicum.booking.Status;
import ru.practicum.item.Item;
import ru.practicum.user.User;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class BookingMapperTest {

    private final BookingMapper bookingMapper = new BookingMapper();

    @Test
    public void testToBookingDto() {
        Booking booking = createTestBooking();

        BookingDto bookingDto = bookingMapper.toBookingDto(booking);

        assertEquals(booking.getId(), bookingDto.getId());
        assertEquals(booking.getStart(), bookingDto.getStart());
        assertEquals(booking.getEnd(), bookingDto.getEnd());
        assertEquals(booking.getItem(), bookingDto.getItem());
        assertEquals(booking.getBooker(), bookingDto.getBooker());
        assertEquals(booking.getStatus(), bookingDto.getStatus());
    }

    @Test
    public void testToBooking() {
        BookingDto bookingDto = createTestBookingDto();

        Booking booking = bookingMapper.toBooking(bookingDto);

        assertEquals(bookingDto.getStart(), booking.getStart());
        assertEquals(bookingDto.getEnd(), booking.getEnd());
    }

    private Booking createTestBooking() {
        Booking booking = new Booking();
        booking.setId(1L);
        booking.setStart(LocalDateTime.now());
        booking.setEnd(LocalDateTime.now().plusHours(2));
        booking.setItem(new Item());
        booking.setBooker(new User());
        booking.setStatus(Status.APPROVED);
        return booking;
    }

    private BookingDto createTestBookingDto() {
        BookingDto bookingDto = BookingDto.builder().build();
        bookingDto.setStart(LocalDateTime.now());
        bookingDto.setEnd(LocalDateTime.now().plusHours(2));
        return bookingDto;
    }
}
