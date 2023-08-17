package ru.practicum.unit;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import ru.practicum.booking.*;
import ru.practicum.exception.BadRequestException;
import ru.practicum.exception.NotFoundException;
import ru.practicum.item.Item;
import ru.practicum.item.ItemRepositoryJpa;
import ru.practicum.user.User;
import ru.practicum.user.UserRepositoryJpa;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.when;

class BookingServiceTest {

    @InjectMocks
    private BookingService bookingService;

    @Mock
    private BookingRepositoryJpa bookingRepository;

    @Mock
    private UserRepositoryJpa userRepository;

    @Mock
    private ItemRepositoryJpa itemRepository;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void add_withInvalidBookingDto_throwsBadRequestException() {
        User user = User.builder()
                .id(1L)
                .name("Test User")
                .email("test@example.com")
                .build();
        Item item = Item.builder()
                .name("Тестовый предмет")
                .available(true)
                .owner(user)
                .description("Тестовое описание")
                .build();
        BookingDto bookingDto = BookingDto.builder()
                .booker(user)
                .item(item)
                .status(Status.APPROVED)
                .build();
        when(itemRepository.findById(1L)).thenReturn(Optional.of(new Item()));
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        assertThrows(NotFoundException.class, () -> bookingService.add(bookingDto, 1L));
    }


    @Test
    void add_withValidBookingDto_returnsBookingDto() {
        User user = User.builder()
                .id(1L)
                .name("Test User")
                .email("test@example.com")
                .build();
        User owner = User.builder()
                .id(2L)
                .name("Test User")
                .email("test@example.com")
                .build();
        Item item = Item.builder()
                .id(1L)
                .name("Тестовый предмет")
                .available(true)
                .owner(owner)
                .description("Тестовое описание")
                .build();
        LocalDateTime testStartTime = LocalDateTime.now();
        BookingDto bookingDto = BookingDto.builder()
                .start(testStartTime.plusHours(1))
                .end(testStartTime.plusHours(2))
                .item(item)
                .booker(user)
                .itemId(1L)
                .status(Status.WAITING)
                .build();
        Booking someMockedBooking = Booking.builder()
                .start(LocalDateTime.now().minusHours(10))
                .end(LocalDateTime.now().plusHours(10))
                .item(item)
                .booker(user)
                .status(Status.WAITING)
                .build();

        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(itemRepository.findById(1L)).thenReturn(Optional.of(item));
        when(bookingRepository.save(any(Booking.class))).thenReturn(someMockedBooking);

        assertNotNull(bookingService.add(bookingDto, 1L));
    }

    @Test
    void update_withInvalidStatus_throwsBadRequestException() {
        User user = User.builder()
                .id(1L)
                .name("Test User")
                .email("test@example.com")
                .build();
        Booking booking = Booking.builder()
                .id(1L)
                .start(LocalDateTime.now().minusHours(10))
                .end(LocalDateTime.now().plusHours(10))
                .booker(user)
                .status(Status.APPROVED)
                .build();
        when(bookingRepository.findById(1L)).thenReturn(Optional.of(booking));
        assertThrows(BadRequestException.class, () -> bookingService.update(1L, 1L, true));
    }

    @Test
    void getByUserId_withInvalidUser_throwsNotFoundException() {
        User user = User.builder()
                .id(2L)
                .name("Test User")
                .email("test@example.com")
                .build();
        Booking booking = Booking.builder()
                .id(2L)
                .start(LocalDateTime.now().minusHours(10))
                .end(LocalDateTime.now().plusHours(10))
                .booker(user)
                .status(Status.APPROVED)
                .build();

        when(bookingRepository.findById(1L)).thenReturn(Optional.of(booking));
        when(userRepository.findById(1L)).thenReturn(Optional.of(new User()));

        assertThrows(NotFoundException.class, () -> bookingService.getByUserId(2L, 2L));
    }

    @Test
    void getAllBooking_withInvalidParams_throwsBadRequestException() {
        User user = User.builder()
                .id(1L)
                .name("Test User")
                .email("test@example.com")
                .build();
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        assertThrows(BadRequestException.class, () -> bookingService.getAllBooking(1L, "ALL", -1, 0));
    }

    @Test
    void getOwnerBooking_withInvalidParams_throwsBadRequestException() {
        User user = User.builder()
                .id(1L)
                .name("Test User")
                .email("test@example.com")
                .build();
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        assertThrows(BadRequestException.class, () -> bookingService.getOwnerBooking(1L, "ALL", -1, 0));
    }
}

