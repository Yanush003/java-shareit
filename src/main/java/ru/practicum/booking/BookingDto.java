package ru.practicum.booking;

import lombok.Builder;
import lombok.Data;
import ru.practicum.item.Item;
import ru.practicum.user.User;

import java.util.Date;

@Data
@Builder
public class BookingDto {
    private Long id;
    private Date start;
    private Date end;
    private Item item;
    private User booker;
    private Status status;
}
