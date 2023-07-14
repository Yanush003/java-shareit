package ru.practicum.user;

import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;

@Data
@Builder
public class UserDto {

    private Long id;
    @NotNull
    private String name;
    @Email
    private String email;
}
