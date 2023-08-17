package ru.practicum.booking;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import ru.practicum.exception.BadRequestException;
import ru.practicum.exception.NotFoundException;
import ru.practicum.item.Item;
import ru.practicum.item.ItemRepositoryJpa;
import ru.practicum.user.User;
import ru.practicum.user.UserRepositoryJpa;

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
    private final BookingRepositoryJpa bookingRepository;
    private final UserRepositoryJpa userRepository;
    private final ItemRepositoryJpa itemRepository;
    private final BookingMapper bookingMapper;

    @Transactional
    public BookingDto add(BookingDto bookingDto, Long userId) {
        User user = getUserById(userId);
        Item item = getItemById(bookingDto.getItemId());
        if (!item.getAvailable()
                || bookingDto.getStart() == null
                || bookingDto.getEnd() == null
                || bookingDto.getEnd().isBefore(LocalDateTime.now())
                || bookingDto.getEnd().isBefore(bookingDto.getStart())
                || bookingDto.getStart().equals(bookingDto.getEnd())
        ) {
            throw new BadRequestException("");
        }
        if (bookingDto.getStart().isBefore(LocalDateTime.now())) {
            throw new BadRequestException("");
        }
        if (Objects.equals(userId, item.getOwner().getId())) {
            throw new NotFoundException("");
        }
        Booking booking = bookingMapper.toBooking(bookingDto);
        booking.setBooker(user);
        booking.setStatus(bookingDto.getStatus() == null ? Status.WAITING : bookingDto.getStatus());
        booking.setItem(item);
        return bookingMapper.toBookingDto(bookingRepository.save(booking));
    }

    @Transactional
    public BookingDto update(Long userId, Long bookingId, Boolean approved) {
        Booking booking = get(bookingId);
        if (booking.getStatus() == (Status.APPROVED) && approved) {
            throw new BadRequestException("");
        }
        Item item = getItemById(booking.getItem().getId());
        if (Objects.equals(item.getOwner().getId(), userId)) {
            booking.setStatus(approved ? Status.APPROVED : Status.REJECTED);
        } else {
            throw new NotFoundException("");
        }
        return bookingMapper.toBookingDto(bookingRepository.save(booking));
    }

    @Transactional
    public BookingDto getByUserId(Long bookingId, Long userId) {
        var booking = get(bookingId);
        getUserById(userId);
        if (!Objects.equals(booking.getBooker().getId(), userId)
                && !Objects.equals(booking.getItem().getOwner().getId(), userId)) {
            throw new NotFoundException("");
        }
        return bookingMapper.toBookingDto(booking);
    }

    @Transactional
    public List<BookingDto> getAllBooking(Long userId, String state, Integer from, Integer size) {
        getUserById(userId);
        if (from < 0 || size <= 0) {
            throw new BadRequestException("");
        }
        int totalElements = bookingRepository.findAllByBooker_Id(userId).size();
        int totalPages = (int) Math.ceil((double) totalElements / size);
        if (from >= totalPages) {
            from = totalPages - 1;
        }
        PageRequest pageRequest = PageRequest.of(from, size, Sort.by("start").descending());
        List<Booking> allByBookerId = bookingRepository.findAllByBookerId(userId, pageRequest).getContent();
        List<Booking> bookings = sortByState(allByBookerId, state);
        return bookings.stream()
                .sorted(Comparator.comparing(Booking::getStart).reversed())
                .map(bookingMapper::toBookingDto)
                .collect(Collectors.toList());
    }

    private Booking get(Long bookingId) {
        return bookingRepository.findById(bookingId)
                .orElseThrow(() -> new NotFoundException(""));
    }

    @Transactional
    public List<BookingDto> getOwnerBooking(Long userId, String state, Integer from, Integer size) {
        getUserById(userId);
        if (from < 0 || size <= 0) {
            throw new BadRequestException("");
        }
        PageRequest pageRequest = PageRequest.of(from, size, Sort.by("start").descending());
        List<Booking> allByItemOwnerId = bookingRepository.findAllByItem_OwnerId(userId, pageRequest).getContent();
        List<Booking> bookings = sortByState(allByItemOwnerId, state);
        return bookings.stream()
                .sorted(Comparator.comparing(Booking::getStart).reversed())
                .map(bookingMapper::toBookingDto)
                .collect(Collectors.toList());
    }

    private User getUserById(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException(""));
    }

    private Item getItemById(Long itemId) {
        return itemRepository.findById(itemId)
                .orElseThrow(() -> new NotFoundException(""));
    }

    public List<Booking> sortByState(List<Booking> allByBookerId, String state) {
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
                return new ArrayList<>();
        }
    }
}
