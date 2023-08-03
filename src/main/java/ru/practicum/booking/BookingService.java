package ru.practicum.booking;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.exception.BadRequestException;
import ru.practicum.exception.NotFoundException;
import ru.practicum.item.Item;
import ru.practicum.item.ItemService;
import ru.practicum.user.User;
import ru.practicum.user.UserService;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BookingService {
    private final BookingRepositoryJpa repository;
    private final UserService userService;
    private final ItemService itemService;

    @Transactional
    public BookingDto add(BookingDto bookingDto, Long userId) {
        User user = userService.get(userId);
        Item item = itemService.get(bookingDto.getItemId());
        if (!item.getAvailable()
                || bookingDto.getStart() == null
                || bookingDto.getEnd() == null
                || bookingDto.getEnd().isBefore(LocalDateTime.now())
                || bookingDto.getEnd().isBefore(bookingDto.getStart())
                || bookingDto.getStart().equals(bookingDto.getEnd())
                || bookingDto.getStart().isBefore(LocalDateTime.now())) {
            throw new BadRequestException("");
        }
        if (Objects.equals(userId, item.getOwner().getId())) {
            throw new NotFoundException("");
        }
        Booking booking = BookingMapper.toBooking(bookingDto);
        booking.setBooker(user);
        booking.setStatus(bookingDto.getStatus() == null ? Status.WAITING : bookingDto.getStatus());
        booking.setItem(item);
        return BookingMapper.toBookingDto(repository.save(booking));
    }


    @Transactional
    public BookingDto update(Long userId, Long bookingId, Boolean approved) {
        Booking booking = get(bookingId);
        if (booking.getStatus() == (Status.APPROVED) && approved) {
            throw new BadRequestException("");
        }
        Item item = itemService.get(booking.getItem().getId());
        if (Objects.equals(item.getOwner().getId(), userId)) {
            booking.setStatus(approved ? Status.APPROVED : Status.REJECTED);
        } else {
            throw new NotFoundException("");
        }
        return BookingMapper.toBookingDto(repository.save(booking));
    }

    @Transactional
    public BookingDto getByUserId(Long bookingId, Long userId) {
        var booking = get(bookingId);
        userService.get(userId);
        if (!Objects.equals(booking.getBooker().getId(), userId)
                && !Objects.equals(booking.getItem().getOwner().getId(), userId)) {
            throw new NotFoundException("");
        }
        return BookingMapper.toBookingDto(booking);
    }


    @Transactional
    public List<BookingDto> getAllBooking(Long userId, String state) {
        userService.get(userId);
        List<Booking> allByBookerId = repository.findAllByBookerId(userId);
        List<Booking> bookings = sortByState(allByBookerId, state);
        return bookings.stream()
                .sorted(Comparator.comparing(Booking::getStart).reversed())
                .map(BookingMapper::toBookingDto)
                .collect(Collectors.toList());
    }

    private Booking get(Long bookingId) {
        return repository.findById(bookingId)
                .orElseThrow(() -> new NotFoundException(""));
    }

    @Transactional
    public List<BookingDto> getOwnerBooking(long userId, String state) {
        userService.get(userId);
        List<Booking> allByItemOwnerId = repository.findAllByItem_OwnerId(userId);
        List<Booking> bookings = sortByState(allByItemOwnerId, state);
        return bookings.stream()
                .sorted(Comparator.comparing(Booking::getStart).reversed())
                .map(BookingMapper::toBookingDto)
                .collect(Collectors.toList());
    }

    private List<Booking> sortByState(List<Booking> allByBookerId, String state) {
        if (state == null) state = "ALL";
        switch (state) {
            case "CURRENT":
                return allByBookerId.stream().filter(x -> x.getStart().isBefore(LocalDateTime.now()) && x.getEnd().isAfter(LocalDateTime.now())).collect(Collectors.toList());
            case "PAST":
                return allByBookerId.stream().filter(x -> x.getEnd().isBefore(LocalDateTime.now())).collect(Collectors.toList());
            case "FUTURE":
                return allByBookerId.stream().filter(x -> x.getStart().isAfter(LocalDateTime.now())).collect(Collectors.toList());
            case "WAITING":
                return allByBookerId.stream().filter(x -> x.getStatus().equals(Status.WAITING)).collect(Collectors.toList());
            case "REJECTED":
                return allByBookerId.stream().filter(x -> x.getStatus().equals(Status.REJECTED)).collect(Collectors.toList());
            case "ALL":
                return new ArrayList<>(allByBookerId);
            default:
                throw new BadRequestException("Unknown state: " + state);
        }
    }
}
