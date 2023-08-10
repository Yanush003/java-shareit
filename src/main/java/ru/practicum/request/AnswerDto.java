package ru.practicum.request;

import lombok.Data;

@Data
public class AnswerDto {
    private Long itemId;
    private String name;
    private String description;
    private Boolean available;
    private Long requestId;
}
