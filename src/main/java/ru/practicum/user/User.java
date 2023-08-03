package ru.practicum.user;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class User {

    private Long id; // — уникальный идентификатор пользователя;
    @NotBlank
    private String name; // — имя или логин пользователя;
    @Email
    private String email; // — адрес электронной почты (учтите, что два пользователя не могут
    //иметь одинаковый адрес электронной почты).
}
