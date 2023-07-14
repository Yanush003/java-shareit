package ru.practicum.request;

import lombok.Builder;
import lombok.Data;
import ru.practicum.user.User;

import java.util.Date;

@Data
@Builder
public class RequestDto {
    private Long id;
    private String description;
    private User requester;
    private Date created;
}
