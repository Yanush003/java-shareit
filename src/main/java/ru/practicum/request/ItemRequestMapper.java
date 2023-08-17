package ru.practicum.request;

import org.springframework.stereotype.Component;

import java.util.List;

@Component
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
