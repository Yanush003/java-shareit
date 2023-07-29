package ru.practicum.booking;

import lombok.Builder;
import lombok.Data;
import ru.practicum.item.Item;
import ru.practicum.user.User;

import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Data
@Builder
public class BookingPostDto {
    private Long id;
    private Long itemId;
    @NotNull
    private LocalDateTime start;
    @NotNull
    private LocalDateTime end;
}
