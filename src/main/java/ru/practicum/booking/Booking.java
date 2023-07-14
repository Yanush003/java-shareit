package ru.practicum.booking;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.practicum.item.Item;
import ru.practicum.user.User;

import java.util.Date;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Booking {
    private Long id; // — уникальный идентификатор бронирования;
    private Date start; // — дата и время начала бронирования;
    private Date end; // — дата и время конца бронирования;
    private Item item; // — вещь, которую пользователь бронирует;
    private User booker; // — пользователь, который осуществляет бронирование;
    private Status status; // — статус бронирования. Может принимать одно из следующих
    // значений: WAITING, APPROVED, REJECTED, CANCELED. (пока сделала Enum Status)

    //отзыв еще должен быть!
}
