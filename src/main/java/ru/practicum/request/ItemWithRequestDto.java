package ru.practicum.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import ru.practicum.item.Item;

@AllArgsConstructor
@Getter
public class ItemWithRequestDto {
    private ItemRequest itemRequest;
    private Item item;
}
