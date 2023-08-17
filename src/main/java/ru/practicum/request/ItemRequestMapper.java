package ru.practicum.request;

import lombok.experimental.UtilityClass;

import java.util.List;

@UtilityClass
public class ItemRequestMapper {
    public ItemRequestDto toItemRequestDto(ItemRequest itemRequest) {

        return ItemRequestDto.builder()
                .id(itemRequest.getId())
                .description(itemRequest.getDescription())
                .requester(itemRequest.getRequester())
                .created(itemRequest.getCreated())
                .build();
    }

    public ItemRequestWithAnswersDto toItemRequestWithAnswersDto(ItemRequest itemRequest, List<AnswerDto> answerDtos) {
        return ItemRequestWithAnswersDto.builder()
                .id(itemRequest.getId())
                .description(itemRequest.getDescription())
                .created(itemRequest.getCreated())
                .items(answerDtos)
                .build();
    }
}

