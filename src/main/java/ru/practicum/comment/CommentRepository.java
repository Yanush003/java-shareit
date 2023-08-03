package ru.practicum.comment;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CommentRepositoryJpa extends JpaRepository<Comment, Long> {
    List<Comment> findAllByItemId(Long itemId);
}
