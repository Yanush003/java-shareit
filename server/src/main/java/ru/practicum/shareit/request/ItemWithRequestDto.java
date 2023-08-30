package ru.practicum.shareit.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import ru.practicum.shareit.item.Item;

@AllArgsConstructor
@Getter
public class ItemWithRequestDto {
    private ItemRequest itemRequest;
    private Item item;
}
