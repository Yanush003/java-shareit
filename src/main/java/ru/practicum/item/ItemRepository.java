package ru.practicum.item;

import ru.practicum.user.User;

import java.util.List;

public interface ItemRepository {

    Item get(Long userId);

    Item add(Long userId, Item item, User user);

    Item update(Long userId, Long itemId, Item item);

    List<Item> search(Long userId, String text);
}
