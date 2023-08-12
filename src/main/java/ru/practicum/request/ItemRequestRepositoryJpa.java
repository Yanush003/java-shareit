package ru.practicum.request;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.user.User;

import java.util.List;

public interface ItemRequestRepositoryJpa extends JpaRepository<ItemRequest, Long> {

    List<ItemRequest> findByRequester(User requester);

    Page<ItemRequest> findByRequester(User user, Pageable pageable);

}
