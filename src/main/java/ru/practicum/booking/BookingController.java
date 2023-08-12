package ru.practicum.booking;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.practicum.exception.BadRequestException;

import java.util.List;

@RestController
@RequestMapping("/bookings")
@RequiredArgsConstructor
public class BookingController {
    private final BookingService bookingService;

    @PostMapping
    public BookingDto add(@RequestHeader("X-Sharer-User-Id") Long userId,
                          @RequestBody BookingDto bookingDto) {
        return bookingService.add(bookingDto, userId);
    }

    @PatchMapping("/{bookingId}")
    public BookingDto updateStatus(@RequestHeader("X-Sharer-User-Id") Long userId,
                                   @PathVariable Long bookingId,
                                   @RequestParam Boolean approved) {
        return bookingService.update(userId, bookingId, approved);
    }

    @GetMapping("/{bookingId}")
    public BookingDto findById(@PathVariable Long bookingId,
                               @RequestHeader("X-Sharer-User-Id") Long userId) {
        return bookingService.getByUserId(bookingId, userId);
    }
    //TODO ПАГИНАЦИЯ from — индекс первого элемента, начиная с 0, и size — количество элементов для отображения.

/*    Реализация пагинации
      Для реализации пагинации используйте возможности, предоставляемые JpaRepository . Вам нужно определить в
      интерфейсе репозитория метод поиска, аналогичный тому, который вы использовали ранее, но принимающий в
      качестве параметра также объект Pageable . Например, для поиска вещи ранее использовался метод List<Item>
      findByOwnerId , создайте метод Page<Item> findByOwnerId(Long ownerId, Pageable pageable) . Тогда всё остальное для
      реализации пагинации на уровне базы данных для вас сделает Spring.
      Вам нужно будет только изменить вызов к данному методу, передавая в качестве дополнительного параметра
      описание требуемой страницы. Для этого используйте метод PageRequest.of(page, size, sort) . Обратите внимание,
      что вам нужно будет преобразовать параметры, передаваемые пользователем, — start и size — к параметрам,
      требуемым Spring, — page и тот же size .*/

    @GetMapping
    public List<BookingDto> getAllBooking(@RequestHeader("X-Sharer-User-Id") Long userId,
                                          @RequestParam(required = false, defaultValue = "ALL") String state) {
        Status.from(state)
                .orElseThrow(() -> new BadRequestException("Unknown state: " + state));
        return bookingService.getAllBooking(userId, state);
    }
    //TODO ПАГИНАЦИЯ from — индекс первого элемента, начиная с 0, и size — количество элементов для отображения.
    @GetMapping("/owner")
    public List<BookingDto> getOwnerBooking(@RequestHeader("X-Sharer-User-Id") Long userId,
                                            @RequestParam(required = false, defaultValue = "ALL") String state) {
        Status.from(state)
                .orElseThrow(() -> new BadRequestException("Unknown state: " + state));
        return bookingService.getOwnerBooking(userId, state);
    }
}
