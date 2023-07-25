package ru.practicum.booking;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.practicum.item.Item;
import ru.practicum.user.User;

import javax.persistence.*;
import java.util.Date;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "bookings")
public class Booking {
    @Column(name = "id")
    @Id
    private Long id; // — уникальный идентификатор бронирования;
    @Column(name = "start_date")
    private Date start; // — дата и время начала бронирования;
    @Column(name = "end_date")
    private Date end; // — дата и время конца бронирования;
    @Transient
    private Item item; // — вещь, которую пользователь бронирует;
    @Transient
    @ManyToOne
    private User booker; // — пользователь, который осуществляет бронирование;
    @Enumerated(EnumType.STRING)
    private Status status; // — статус бронирования. Может принимать одно из следующих
    // значений: WAITING, APPROVED, REJECTED, CANCELED. (пока сделала Enum Status)

}
