package ru.practicum.shareit.itemrequest.dto;

import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class ItemRequestDescriptionDto {
    @NotBlank
    private String description;
}
