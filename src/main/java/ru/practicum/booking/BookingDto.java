package ru.practicum.booking;

import lombok.Builder;
import lombok.Data;
import ru.practicum.item.Item;
import ru.practicum.user.User;

import java.time.LocalDateTime;

@Data
@Builder
public class BookingDto {
    private Long id;
    private LocalDateTime start;
    private LocalDateTime end;
    private Long itemId;
    private Item item;
    private Status status;
    private User booker;
    private Status state;
}
