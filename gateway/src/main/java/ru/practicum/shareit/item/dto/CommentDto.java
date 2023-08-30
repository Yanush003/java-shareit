package ru.practicum.shareit.item.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;

@Setter
@Getter
@Builder
public class CommentDto {
    private Long id;
    @NotBlank
    @Size(min = 2, max = 512)
    private String text;
    private String authorName;
    private LocalDateTime created;
}
