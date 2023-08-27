package ru.practicum.shareit.item;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.shareit.request.ItemRequest;
import ru.practicum.shareit.user.User;

import java.util.List;

public interface ItemRepositoryJpa extends JpaRepository<Item, Long> {
    List<Item> findAllByOwner(User user);

    List<Item> findAllByRequest(ItemRequest itemRequest);
}
