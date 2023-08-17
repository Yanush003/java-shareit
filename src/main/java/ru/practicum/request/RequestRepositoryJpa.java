package ru.practicum.request;

import org.springframework.data.jpa.repository.JpaRepository;

public interface RequestRepositoryJpa extends JpaRepository<ItemRequest, Long> {
}
