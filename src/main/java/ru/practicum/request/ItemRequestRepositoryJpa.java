package ru.practicum.request;

import org.springframework.data.jpa.repository.JpaRepository;

public interface ItemRequestRepositoryJpa extends JpaRepository<ItemRequest, Long> {
}
