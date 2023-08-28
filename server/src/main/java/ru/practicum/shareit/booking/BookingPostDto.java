package ru.practicum.shareit.booking;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class BookingPostDto {
    private Long id;
    private Long itemId;
    private LocalDateTime start;
    private LocalDateTime end;
}
