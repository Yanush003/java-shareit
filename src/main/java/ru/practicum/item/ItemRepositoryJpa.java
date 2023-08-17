package ru.practicum.item;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.user.User;

import java.util.List;

public interface ItemRepositoryJpa extends JpaRepository<Item, Long> {
    List<Item> findAllByOwner(User user);
}
