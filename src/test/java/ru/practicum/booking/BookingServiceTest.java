package ru.practicum.booking;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import ru.practicum.item.Item;
import ru.practicum.item.ItemRepositoryJpa;
import ru.practicum.user.User;
import ru.practicum.user.UserRepositoryJpa;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Optional;

class BookingServiceTest {

    private BookingRepositoryJpa bookingRepository = Mockito.mock(BookingRepositoryJpa.class);
    private UserRepositoryJpa userRepository = Mockito.mock(UserRepositoryJpa.class);
    private ItemRepositoryJpa itemRepository = Mockito.mock(ItemRepositoryJpa.class);

    private BookingService bookingService = new BookingService(bookingRepository, userRepository, itemRepository);

    @Test
    void addTest() {
        User expectedUser = new User(null, "ury", "yyy@ya.ru");
        Item expectedItem = new Item();
        expectedItem.setAvailable(true);
        User owner = new User();
        owner.setId(2L);
        expectedItem.setOwner(owner);
        BookingDto expectedBookingDto = new BookingDto(null,
                LocalDateTime.now().plus(1, ChronoUnit.SECONDS),
                LocalDateTime.now().plus(1, ChronoUnit.MINUTES),
                1L,
                null,
                null,
                null,
                Status.WAITING);
        Mockito.when(userRepository.findById(1L)).thenReturn(Optional.of(expectedUser));
        Mockito.when(itemRepository.findById(1L)).thenReturn(Optional.of(expectedItem));
        Mockito.when(bookingRepository.save(ArgumentMatchers.any(Booking.class))).thenAnswer(invocationOnMock -> {
            Booking booking = invocationOnMock.getArgument(0);
            booking.setId(1L);
            return booking;
        });
        BookingDto actualResult = bookingService.add(expectedBookingDto, 1L);
        BookingDto expectedBD = new BookingDto(1L, expectedBookingDto.getStart(), expectedBookingDto.getEnd(), null, expectedItem, Status.WAITING, expectedUser, null);
        Assertions.assertNotNull(actualResult);
        Assertions.assertEquals(expectedBD, actualResult);
    }

    @Test
    void updateTest() {
        Booking expectedBooking = new Booking();

    }

    @Test
    void getByUserIdTest() {

    }

    @Test
    void getAllBookingTest() {

    }

    @Test
    void getOwnerBookingTest() {

    }
}
