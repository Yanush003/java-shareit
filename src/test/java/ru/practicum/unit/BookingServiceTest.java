package ru.practicum.unit;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import ru.practicum.booking.*;
import ru.practicum.exception.BadRequestException;
import ru.practicum.exception.NotFoundException;
import ru.practicum.item.Item;
import ru.practicum.item.ItemRepositoryJpa;
import ru.practicum.user.User;
import ru.practicum.user.UserRepositoryJpa;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class BookingServiceTest {

    @InjectMocks
    private BookingService bookingService;

    @Mock
    private BookingRepositoryJpa bookingRepository;

    @Mock
    private UserRepositoryJpa userRepository;

    @Mock
    private ItemRepositoryJpa itemRepository;

    @Mock
    private BookingMapper bookingMapper;

    private List<Booking> bookings;
    private LocalDateTime now;
    private User mockUser;
    private Item mockItem;
    private Booking mockBookingWithApproved;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockUser = User.builder()
                .id(1L)
                .name("Test User")
                .email("test@example.com")
                .build();
        mockItem = Item.builder()
                .id(1L)
                .name("Тестовый предмет")
                .available(true)
                .description("Тестовое описание")
                .build();
        mockBookingWithApproved = Booking.builder()
                .id(2L)
                .start(LocalDateTime.now().minusHours(10))
                .end(LocalDateTime.now().plusHours(10))
                .booker(mockUser)
                .status(Status.APPROVED)
                .build();
        now = LocalDateTime.now();
        bookings = Arrays.asList(
                Booking.builder().start(now.minusDays(2)).end(now.minusDays(1)).status(Status.WAITING).build(),
                Booking.builder().start(now.minusHours(1)).end(now.plusHours(1)).status(Status.APPROVED).build(),
                Booking.builder().start(now.plusDays(1)).end(now.plusDays(2)).status(Status.WAITING).build(),
                Booking.builder().start(now.plusDays(3)).end(now.plusDays(4)).status(Status.REJECTED).build()
        );
    }

    @Test
    void add_withInvalidBookingDto_throwsBadRequestException() {
        Item item = Item.builder()
                .name("Тестовый предмет")
                .available(true)
                .owner(mockUser)
                .description("Тестовое описание")
                .build();
        BookingDto bookingDto = BookingDto.builder()
                .booker(mockUser)
                .item(item)
                .status(Status.APPROVED)
                .build();
        when(itemRepository.findById(1L)).thenReturn(Optional.of(new Item()));
        when(userRepository.findById(1L)).thenReturn(Optional.of(mockUser));
        assertThrows(NotFoundException.class, () -> bookingService.add(bookingDto, 1L));
    }

    @Test
    void add_withValidBookingDto_returnsBookingDto() {
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
                .booker(mockUser)
                .itemId(1L)
                .status(Status.WAITING)
                .build();
        Booking booking = Booking.builder()
                .start(LocalDateTime.now().minusHours(10))
                .end(LocalDateTime.now().plusHours(10))
                .item(item)
                .booker(mockUser)
                .status(Status.WAITING)
                .build();
        when(userRepository.findById(1L)).thenReturn(Optional.of(mockUser));
        when(itemRepository.findById(1L)).thenReturn(Optional.of(item));
        when(bookingRepository.save(any(Booking.class))).thenReturn(booking);
        when(bookingMapper.toBooking(bookingDto)).thenReturn(booking);
        when(bookingMapper.toBookingDto(booking)).thenReturn(bookingDto);
        assertNotNull(bookingService.add(bookingDto, 1L));
    }

    @Test
    void update_withInvalidStatus_throwsBadRequestException() {
        when(bookingRepository.findById(1L)).thenReturn(Optional.of(mockBookingWithApproved));
        assertThrows(BadRequestException.class, () -> bookingService.update(1L, 1L, true));
    }

    @Test
    void getByUserId_withInvalidUser_throwsNotFoundException() {
        when(bookingRepository.findById(1L)).thenReturn(Optional.of(mockBookingWithApproved));
        when(userRepository.findById(1L)).thenReturn(Optional.of(new User()));
        assertThrows(NotFoundException.class, () -> bookingService.getByUserId(2L, 2L));
    }

    @Test
    void getAllBooking_withInvalidParams_throwsBadRequestException() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(mockUser));
        assertThrows(BadRequestException.class, () -> bookingService.getAllBooking(1L, "ALL", -1, 0));
    }

    @Test
    void getOwnerBooking_withInvalidParams_throwsBadRequestException() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(mockUser));
        assertThrows(BadRequestException.class, () -> bookingService.getOwnerBooking(1L, "ALL", -1, 0));
    }

    @Test
    void getByUserId_withMismatchedUserId_throwsNotFoundException() {
        when(bookingRepository.findById(1L)).thenReturn(Optional.of(mockBooking()));
        assertThrows(NotFoundException.class, () -> bookingService.getByUserId(1L, 2L));
    }

    @Test
    void update_withAlreadyApprovedStatus_throwsBadRequestException() {
        Booking booking = mockBooking();
        booking.setStatus(Status.APPROVED);
        when(bookingRepository.findById(1L)).thenReturn(Optional.of(booking));
        assertThrows(BadRequestException.class, () -> bookingService.update(1L, 1L, true));
    }

    @Test
    void getAllBooking_withInvalidPagination_throwsBadRequestException() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(mockUser));
        assertThrows(BadRequestException.class, () -> bookingService.getAllBooking(1L, "ALL", -1, 10));
        assertThrows(BadRequestException.class, () -> bookingService.getAllBooking(1L, "ALL", 0, 0));
    }

    @Test
    void getOwnerBooking_withInvalidPagination_throwsBadRequestException() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(mockUser));
        assertThrows(BadRequestException.class, () -> bookingService.getOwnerBooking(1L, "ALL", -1, 10));
        assertThrows(BadRequestException.class, () -> bookingService.getOwnerBooking(1L, "ALL", 0, 0));
    }

    @Test
    void sortByState_withUnknownState_returnsEmptyList() {
        List<Booking> result = bookingService.sortByState(Collections.singletonList(mockBooking()), "UNKNOWN");
        assertTrue(result.isEmpty());
    }

    @Test
    public void testSortByStateCurrent() {
        List<Booking> result = bookingService.sortByState(bookings, "CURRENT");
        assertEquals(1, result.size());
        assertEquals(now.minusHours(1), result.get(0).getStart());
    }

    @Test
    public void testSortByStatePast() {
        List<Booking> result = bookingService.sortByState(bookings, "PAST");
        assertEquals(1, result.size());
        assertEquals(now.minusDays(2), result.get(0).getStart());
    }

    @Test
    public void testSortByStateFuture() {
        List<Booking> result = bookingService.sortByState(bookings, "FUTURE");
        assertEquals(2, result.size());
    }

    @Test
    public void testSortByStateWaiting() {
        List<Booking> result = bookingService.sortByState(bookings, "WAITING");
        assertEquals(2, result.size());
    }

    @Test
    public void testSortByStateRejected() {
        List<Booking> result = bookingService.sortByState(bookings, "REJECTED");
        assertEquals(1, result.size());
        assertEquals(Status.REJECTED, result.get(0).getStatus());
    }

    @Test
    public void testSortByStateAll() {
        List<Booking> result = bookingService.sortByState(bookings, "ALL");
        assertEquals(4, result.size());
    }

    @Test
    public void testSortByStateDefault() {
        List<Booking> result = bookingService.sortByState(bookings, "UNKNOWN_STATE");
        assertEquals(0, result.size());
    }

    @Test
    void add_whenItemNotFound_throwsNotFoundException() {
        BookingDto bookingDto = BookingDto.builder()
                .itemId(1L)
                .booker(mockUser)
                .build();
        when(itemRepository.findById(1L)).thenReturn(Optional.empty());
        assertThrows(NotFoundException.class, () -> bookingService.add(bookingDto, 1L));
    }

    @Test
    void add_whenUserNotFound_throwsNotFoundException() {
        BookingDto bookingDto = BookingDto.builder()
                .itemId(1L)
                .booker(mockUser)
                .build();

        when(userRepository.findById(1L)).thenReturn(Optional.empty());
        assertThrows(NotFoundException.class, () -> bookingService.add(bookingDto, 1L));
    }

    @Test
    void update_whenBookingNotFound_throwsNotFoundException() {
        when(bookingRepository.findById(1L)).thenReturn(Optional.empty());
        assertThrows(NotFoundException.class, () -> bookingService.update(1L, 1L, true));
    }

    @Test
    void update_whenBookingIsAlreadyRejected_throwsBadRequestException() {
        Booking booking = mockBooking();
        booking.setStatus(Status.REJECTED);
        when(bookingRepository.findById(1L)).thenReturn(Optional.of(booking));
        assertThrows(NotFoundException.class, () -> bookingService.update(1L, 1L, false));
    }


    @Test
    void add_withUnavailableItem_throwsBadRequestException() {
        Item item = Item.builder().id(1L).name("Тестовый предмет").available(false).description("Тестовое описание").build();
        BookingDto bookingDto = BookingDto.builder().booker(mockUser).item(item).status(Status.APPROVED).build();
        when(userRepository.findById(1L)).thenReturn(Optional.of(mockUser));
        when(itemRepository.findById(1L)).thenReturn(Optional.of(item));
        assertThrows(NotFoundException.class, () -> bookingService.add(bookingDto, 1L));
    }

    @Test
    void add_withInvalidItem_throwsNotFoundException() {
        BookingDto bookingDto = BookingDto.builder().booker(mockUser).itemId(99L).status(Status.APPROVED).build();
        when(userRepository.findById(1L)).thenReturn(Optional.of(mockUser));
        when(itemRepository.findById(99L)).thenReturn(Optional.empty());
        assertThrows(NotFoundException.class, () -> bookingService.add(bookingDto, 1L));
    }

    @Test
    void getByUserId_withMismatchedUserId_throwsBadRequestException() {
        Item item = Item.builder().id(1L).name("Тестовый предмет").available(true).description("Тестовое описание").build();
        Booking bookingDto = Booking.builder().booker(mockUser).item(item).status(Status.APPROVED).build();
        when(userRepository.findById(2L)).thenReturn(Optional.of(mockUser));
        when(bookingRepository.findById(1L)).thenReturn(Optional.of(bookingDto));
        assertThrows(NullPointerException.class, () -> bookingService.getByUserId(1L, 2L));
    }

    @Test
    void updateBooking_withNonexistentBooking_throwsNotFoundException() {
        when(bookingRepository.findById(1L)).thenReturn(Optional.empty());
        assertThrows(NotFoundException.class, () -> bookingService.update(1L, 1L, true));
    }


    @Test
    void getByUserId_withValidUser_returnsBookingDto() {
        Booking booking = Booking.builder()
                .booker(mockUser)
                .item(mockItem)
                .status(Status.APPROVED)
                .build();
        BookingDto bookingDtoExpected = BookingDto.builder()
                .booker(mockUser)
                .item(mockItem)
                .status(Status.APPROVED)
                .build();
        when(bookingRepository.findById(1L)).thenReturn(Optional.of(booking));
        when(userRepository.findById(1L)).thenReturn(Optional.of(mockUser));
        when(bookingMapper.toBookingDto(booking)).thenReturn(bookingDtoExpected);
        BookingDto bookingDtoResult = bookingService.getByUserId(1L, 1L);
        assertEquals(bookingDtoExpected, bookingDtoResult);
    }

    @Test
    void add_whenBookingMapperFails_throwsException() {
        BookingDto bookingDto = BookingDto.builder()
                .itemId(1L)
                .booker(mockUser)
                .build();
        when(userRepository.findById(1L)).thenReturn(Optional.of(mockUser));
        when(itemRepository.findById(1L)).thenReturn(Optional.of(mockItem()));
        when(bookingMapper.toBooking(bookingDto)).thenThrow(new RuntimeException("Mapper error"));
        assertThrows(RuntimeException.class, () -> bookingService.add(bookingDto, 1L));
    }

    @Test
    void getByUserId_withInvalidUser_throwsUserNotFoundException() {
        when(userRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> bookingService.getByUserId(1L, 1L));
    }

    @Test
    void getByUserId_withoutBookingForUser_throwsBookingNotFoundException() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(mockUser));
        when(bookingRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> bookingService.getByUserId(1L, 1L));
    }

    @Test
    void getByUserId_withRejectedStatus_throwsInvalidStatusException() {
        Booking booking = Booking.builder()
                .booker(mockUser)
                .item(mockItem)
                .status(Status.REJECTED)
                .build();
        BookingDto bookingDto = bookingMapper.toBookingDto(booking);
        when(userRepository.findById(1L)).thenReturn(Optional.of(mockUser));
        when(bookingRepository.findById(1L)).thenReturn(Optional.of(booking));
        when(bookingMapper.toBookingDto(booking)).thenReturn(bookingDto);
        assertThrows(NotFoundException.class, () -> bookingService.getByUserId(1L, 2L));
    }

    @Test
    public void update_shouldSetStatusBasedOnApproval_whenUserIdMatchesOwnerId() {
        Long userId = 1L;
        Long bookingId = 2L;
        Boolean approved = true;
        Booking mockBooking = Booking.builder()
                .item(Item.builder()
                        .id(userId)
                        .owner(User.builder()
                                .id(userId)
                                .build())
                        .build())
                .build();

        when(bookingRepository.findById(bookingId)).thenReturn(Optional.of(mockBooking));
        when(itemRepository.findById(anyLong())).thenReturn(Optional.of(mockBooking.getItem()));
        when(bookingMapper.toBookingDto(any())).thenReturn(BookingDto.builder()
                .itemId(1L)
                .booker(mockUser)
                .build());
        BookingDto update = bookingService.update(userId, bookingId, approved);
        assertEquals(Status.APPROVED, mockBooking.getStatus());
        verify(bookingRepository).save(mockBooking);
    }

    @Test
    public void update_shouldThrowNotFoundException_whenUserIdDoesNotMatchOwnerId() {
        Long userId = 1L;
        Long differentUserId = 2L;
        Long bookingId = 2L;
        Boolean approved = true;
        Booking mockBooking = Booking.builder()
                .item(Item.builder()
                        .owner(User.builder()
                                .id(differentUserId)
                                .build())
                        .build())
                .build();
        when(bookingRepository.findById(bookingId)).thenReturn(Optional.of(mockBooking));
        when(itemRepository.findById(anyLong())).thenReturn(Optional.of(mockBooking.getItem()));
        assertThrows(NotFoundException.class, () -> {
            bookingService.update(userId, bookingId, approved);
        });
    }

    @Test
    public void update_shouldThrowBadRequestException_whenBookingIsAlreadyApproved() {
        Long userId = 1L;
        Long bookingId = 2L;
        Boolean approved = true;

        Booking mockBooking = Booking.builder()
                .status(Status.APPROVED)
                .build();

        when(bookingRepository.findById(bookingId)).thenReturn(Optional.of(mockBooking));

        assertThrows(BadRequestException.class, () -> {
            bookingService.update(userId, bookingId, approved);
        });
    }

    @Test
    public void getAllBooking_shouldThrowBadRequest_whenFromIsNegative() {
        Long userId = 1L;
        assertThrows(BadRequestException.class, () -> {
            bookingService.getAllBooking(userId, "someState", -1, 5);
        });
    }

    @Test
    public void getAllBooking_shouldThrowBadRequest_whenSizeIsNonPositive() {
        Long userId = 1L;
        assertThrows(BadRequestException.class, () -> {
            bookingService.getAllBooking(userId, "someState", 0, 0);
        });
    }

    @Test
    public void getAllBooking_shouldAdjustFrom_whenItExceedsTotalPages() {
        Long userId = 1L;
        int size = 5;
        int totalBookings = 12;
        int from = 3;
        List<Booking> bookings = IntStream.range(0, totalBookings)
                .mapToObj(i -> new Booking())
                .collect(Collectors.toList());
        when(bookingRepository.findAllByBooker_Id(userId)).thenReturn(bookings);
        when(userRepository.findById(userId)).thenReturn(Optional.of(new User()));
        PageRequest pageRequest = PageRequest.of(2, size, Sort.by("start").descending());
        when(bookingRepository.findAllByBookerId(ArgumentMatchers.eq(userId), ArgumentMatchers.eq(pageRequest)))
                .thenReturn(Page.empty());
        bookingService.getAllBooking(userId, "WAITING", from, size);
        verify(bookingRepository).findAllByBookerId(eq(userId), eq(pageRequest));
    }

    @Test
    public void getAllBooking_shouldReturnBookingsSortedByStateAndDate() {
        Long userId = 1L;
        int size = 5;
        int from = 0;
        List<Booking> mockBookings = Arrays.asList(
                createMockBooking(1L, LocalDateTime.now().minusDays(1), Status.WAITING),
                createMockBooking(2L, LocalDateTime.now().minusDays(2), Status.WAITING),
                createMockBooking(3L, LocalDateTime.now().minusDays(3), Status.WAITING)
        );
        List<BookingDto> mockDtos = mockBookings.stream()
                .map(b -> BookingDto.builder().build())
                .collect(Collectors.toList());

        when(bookingRepository.findAllByBooker_Id(userId)).thenReturn(mockBookings);
        for (int i = 0; i < mockBookings.size(); i++) {
            when(bookingMapper.toBookingDto(mockBookings.get(i))).thenReturn(mockDtos.get(i));
        }
        when(userRepository.findById(userId)).thenReturn(Optional.of(new User()));
        PageRequest pageRequest = PageRequest.of(from, size, Sort.by("start").descending());
        when(bookingRepository.findAllByBookerId(ArgumentMatchers.eq(userId), ArgumentMatchers.eq(pageRequest)))
                .thenReturn(new PageImpl<>(mockBookings));
        List<BookingDto> result = bookingService.getAllBooking(userId, "WAITING", from, size);

        assertEquals(mockDtos, result);
    }

    private Item mockItem() {
        Item item = new Item();
        item.setId(1L);
        item.setOwner(mockUser);
        return item;
    }

    private Booking mockBooking() {
        Booking booking = new Booking();
        booking.setId(1L);
        booking.setBooker(mockUser);
        booking.setItem(mockItem());
        return booking;
    }

    private Booking createMockBooking(Long id, LocalDateTime start, Status status) {
        return Booking.builder()
                .id(id)
                .start(start)
                .end(start.plusHours(2))
                .item(new Item())
                .booker(new User())
                .status(status)
                .build();
    }
}
