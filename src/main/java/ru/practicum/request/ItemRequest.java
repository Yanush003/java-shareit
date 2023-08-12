package ru.practicum.request;

import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import ru.practicum.user.User;

import javax.persistence.*;
import java.time.LocalDateTime;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "request")
public class ItemRequest {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    private Long id; // — уникальный идентификатор запроса;
    @Column(name = "description")
    private String description; // — текст запроса, содержащий описание требуемой вещи;
    @ManyToOne
    @JoinColumn(name = "requester_id")
    private User requester; // — пользователь, создавший запрос;
    @CreationTimestamp
    @Column(name = "created")
    private LocalDateTime created; // — дата и время создания запроса.
}
