package ru.practicum.request;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
public class ItemRequestWithAnswersDto {
    private Long id;
    private String description;
    private LocalDateTime created;
    private List<AnswerDto> items;
}
