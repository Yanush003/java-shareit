package ru.practicum.request;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.item.Item;
import ru.practicum.user.User;

import java.util.List;

public interface ItemRequestRepositoryJpa extends JpaRepository<ItemRequest, Long> {
    List<ItemRequest> findByRequester(User requester);

    @Query("select i from Item i JOIN i.request r where i.owner = :user")
    Page<Item> findByRequester(User user, Pageable pageable);
}
