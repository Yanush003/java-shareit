package ru.practicum.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.practicum.user.User;

import javax.persistence.*;
import java.util.Date;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "request")
public class ItemRequest {
    @Column(name = "id")
    @Id
    private Long id; // — уникальный идентификатор запроса;
    @Column(name = "description")
    private String description; // — текст запроса, содержащий описание требуемой вещи;
    @Transient
    @ManyToOne
    private User requester; // — пользователь, создавший запрос;
    @Column(name = "created")
    private Date created; // — дата и время создания запроса.
}
