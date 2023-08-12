package ru.practicum.request;

import lombok.Builder;
import lombok.Data;
import ru.practicum.user.User;

import java.time.LocalDateTime;
import java.util.Date;

@Data
@Builder
public class ItemRequestDto {
    private Long id;
    private String description;
    private User requester; //Dto
    private LocalDateTime created; //Dto
}