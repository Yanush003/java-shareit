package ru.practicum.booking;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.practicum.item.Item;
import ru.practicum.user.User;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "bookings")
public class Booking {
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    @Column(name = "id")
    @Id
    private Long id; // — уникальный идентификатор бронирования;
    @Column(name = "start_date")
    private LocalDateTime start; // — дата и время начала бронирования;
    @Column(name = "end_date")
    private LocalDateTime end; // — дата и время конца бронирования;
    @ManyToOne (fetch = FetchType.EAGER)
    @JoinColumn(name = "item_id")
    private Item item; // — вещь, которую пользователь бронирует;
    @ManyToOne (fetch = FetchType.EAGER)
    @JoinColumn(name = "booker_id")
    private User booker; // — пользователь, который осуществляет бронирование;
    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private Status status; // — статус бронирования. Может принимать одно из следующих
    // значений: WAITING, APPROVED, REJECTED, CANCELED. (пока сделала Enum Status)

}
