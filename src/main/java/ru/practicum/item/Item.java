package ru.practicum.item;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.practicum.request.ItemRequest;
import ru.practicum.user.User;

import javax.validation.constraints.NotNull;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Item {

    private Long id;
    @NotNull
    private String name;
    private String description;
    private Boolean available; //— статус о том, доступна или нет вещь для аренды; Статус должен проставлять владелец.
    private User owner; //— владелец вещи;
    private ItemRequest request; // если вещь была создана по запросу другого пользователя, то в этом
    // поле будет храниться ссылка на соответствующий запрос
}
