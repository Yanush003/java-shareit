package ru.practicum.request;

import lombok.experimental.UtilityClass;
import ru.practicum.item.Item;
import ru.practicum.item.ItemDto;
import ru.practicum.user.User;
import ru.practicum.user.UserDto;

import java.util.List;

@UtilityClass
public class ItemRequestMapper {
    public ItemRequestDto toItemRequestDto(ItemRequest itemRequest) {

        //UserDto (под тесты) (id, name)
        return ItemRequestDto.builder()
                .id(itemRequest.getId())
                .description(itemRequest.getDescription())
                .requester(itemRequest.getRequester())
                .created(itemRequest.getCreated())
                .build();
    }

    // обратно toItemRequest (ItemRequestDto, User)
    public UserDto toUserDto(User user) {
        return UserDto.builder()
                .id(user.getId())
                .name(user.getName())
                .build();
    }

    public ItemRequest toItemRequest(ItemRequestDto itemRequestDto) {
        return ItemRequest.builder()
                .id(itemRequestDto.getId())
                .description(itemRequestDto.getDescription())
                .requester(itemRequestDto.getRequester())
                .created(itemRequestDto.getCreated())
                .build();
    }

    public ItemRequestWithAnswersDto toItemRequestWithAnswersDto(ItemRequest itemRequest, List<AnswerDto> answerDtos){
        return  ItemRequestWithAnswersDto.builder()
                .id(itemRequest.getId())
                .description(itemRequest.getDescription())
                .created(itemRequest.getCreated())
                .items(answerDtos)
                .build();
    }
}

