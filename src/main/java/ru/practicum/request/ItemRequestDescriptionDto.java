package ru.practicum.request;

import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class ItemRequestDescriptionDto {
    @NotBlank
    private String description;
}
