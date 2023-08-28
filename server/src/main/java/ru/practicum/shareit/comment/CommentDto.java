package ru.practicum.shareit.comment;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Setter
@Getter
@Builder
public class CommentDto {
    private Long id;

    private String text;
    private String authorName;
    private LocalDateTime created;
}
