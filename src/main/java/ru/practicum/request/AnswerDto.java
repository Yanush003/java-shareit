package ru.practicum.request;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class AnswerDto {
    private Long id;
    private String name;
    private String description;
    private Boolean available;
    private Long requestId;
}
