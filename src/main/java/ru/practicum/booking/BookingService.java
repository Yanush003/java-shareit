package ru.practicum.booking;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.exception.NotFoundException;
import ru.practicum.exception.WrongDataException;
import ru.practicum.item.Item;
import ru.practicum.item.ItemService;
import ru.practicum.user.User;
import ru.practicum.user.UserService;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BookingService {
    private final BookingRepositoryJpa repository;
    private final UserService userService;
    private final ItemService itemService;

    public BookingDto add(Long userId, BookingPostDto bookingDto) {
        LocalDateTime now = LocalDateTime.now().truncatedTo(ChronoUnit.MINUTES);
        if (bookingDto.getStart().isBefore(now)) {
            throw new WrongDataException("");
        }

        if (!bookingDto.getStart().isBefore(bookingDto.getEnd())) {
            throw new WrongDataException("");
        }

        User user = userService.get(userId);
        Item item = itemService.get(bookingDto.getItemId());
        if (!item.getAvailable()) {
            throw new WrongDataException("Item " + item.getId() + " not available");
        }

        Booking booking = Booking.builder()
                .booker(user)
                .item(item)
                .start(bookingDto.getStart())
                .end(bookingDto.getEnd())
                .status(Status.WAITING)
                .build();
        Booking save = repository.save(booking);
        return BookingMapper.toBookingDto(save);
    }

    //Подтверждение или отклонение запроса на бронирование.
    // Может быть выполнено только владельцем вещи.
    // Затем статус бронирования становится либо APPROVED, либо REJECTED.
    // параметр approved может принимать значения true или false.
    //TODO УДАЛИТЬ itemDto везде
    public Booking update(Long userId, Long bookingId, Boolean approved) {
        Booking booking = get(bookingId);
        if (!booking.getItem().getOwner().getId().equals(userId)) {
            throw new NotFoundException("");
        }
        if (approved) {
            booking.setStatus(Status.APPROVED);
        } else {
            booking.setStatus(Status.REJECTED);
        }
        Booking save = repository.save(booking);
        return save;
    }

    public Booking getByUserId(Long userId, Long bookingId) {
        Booking booking = get(bookingId);
        if (booking.getBooker().getId().equals(userId) || booking.getItem().getOwner().getId().equals(userId)) {
            return booking;
        } else {
            throw new NotFoundException("");
        }
    }

    private Booking get(Long bookingId) {
        return repository.findById(bookingId)
                .orElseThrow(() -> new NotFoundException("Booking with id=" + bookingId + " not exist"));
    }

    //Получение списка всех бронирований текущего пользователя.
    // Параметр state необязательный и по умолчанию равен ALL (англ. «все»).
    // Также он может принимать значения CURRENT (англ. «текущие»), **PAST** (англ. «завершённые»),
    // FUTURE (англ. «будущие»), WAITING (англ. «ожидающие подтверждения»), REJECTED (англ. «отклонённые»).
    // Бронирования должны возвращаться отсортированными по дате от более новых к более старым.
    public List<Booking> getAllBooking(Long userId, String state) {
        return repository.findAllByBooker_IdOrderByStart(userId, state);
    }

    // Получение списка бронирований для всех вещей текущего пользователя.
    // Этот запрос имеет смысл для владельца хотя бы одной вещи.
    // Работа параметра state аналогична его работе в предыдущем сценарии.
    public List<Booking> getOwnerBooking(Long userId, String state) {
        userService.get(userId);
        return repository.findAllByOwner_IdOrderByStart((userId), state);
    }

    private List<Booking> filterByState(List<Booking> allByBookerId, String state) {
        switch (state) {
            case "CURRENT":
                return allByBookerId.stream().filter(x -> x.getStatus().equals(Status.APPROVED)).collect(Collectors.toList());
            case "PAST":
                return allByBookerId.stream().filter(x -> x.getEnd().isBefore(LocalDateTime.now())).collect(Collectors.toList());
            case "FUTURE":
                return allByBookerId.stream().filter(x -> x.getStart().isAfter(LocalDateTime.now())).collect(Collectors.toList());
            case "WAITING":
                return allByBookerId.stream().filter(x -> x.getStatus().equals(Status.WAITING)).collect(Collectors.toList());
            case "REJECTED":
                return allByBookerId.stream().filter(x -> x.getStatus().equals(Status.REJECTED)).collect(Collectors.toList());
            default:
                return allByBookerId;
        }
    }
}
