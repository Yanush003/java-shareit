package ru.practicum.request;

import lombok.Builder;
import lombok.Data;
import ru.practicum.user.User;

import java.util.Date;

@Builder
@Data
public class ItemRequest {
    private Long id; // — уникальный идентификатор запроса;
    private String description; // — текст запроса, содержащий описание требуемой вещи;
    private User requester; // — пользователь, создавший запрос;
    private Date created; // — дата и время создания запроса.
}
