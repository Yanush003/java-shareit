package ru.practicum.item;

import lombok.Builder;
import lombok.Data;
import ru.practicum.request.ItemRequest;
import ru.practicum.user.User;

import javax.validation.constraints.NotNull;

@Data
@Builder
public class ItemDto {
    private Long id;
    @NotNull
    private String name;
    private String description;
    @NotNull
    private Boolean available; //— статус о том, доступна или нет вещь для аренды;
    private User owner;
    private ItemRequest request;
}
