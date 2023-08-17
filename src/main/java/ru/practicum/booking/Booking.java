package ru.practicum.booking;

import lombok.*;
import ru.practicum.item.Item;
import ru.practicum.user.User;

import javax.persistence.*;
import java.time.LocalDateTime;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@Entity
@Table(name = "bookings")
public class Booking {

    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    private Long id; // — уникальный идентификатор бронирования;
    @Column(name = "start_date")
    private LocalDateTime start; // — дата и время начала бронирования;
    @Column(name = "end_date")
    private LocalDateTime end; // — дата и время конца бронирования;
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "item_id")
    private Item item; // — вещь, которую пользователь бронирует;
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "booker_id")
    private User booker; // — пользователь, который осуществляет бронирование;
    @Enumerated(EnumType.STRING)
    private Status status; // — статус бронирования. Может принимать одно из следующих
    // значений: WAITING, APPROVED, REJECTED, CANCELED. (пока сделала Enum Status)
}
