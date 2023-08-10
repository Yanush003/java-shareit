package ru.practicum.request;

import ru.practicum.user.User;
import ru.practicum.user.UserDto;

public class ItemRequestMapper {
    public static ItemRequestDto toItemRequestDto(ItemRequest itemRequest) {

        //UserDto (под тесты) (id, name)
        return ItemRequestDto.builder()
                .id(itemRequest.getId())
                .description(itemRequest.getDescription())
                .requester(itemRequest.getRequester())
                .created(itemRequest.getCreated())
                .build();
    }

    // обратно toItemRequest (ItemRequestDto, User)
    public static UserDto toUserDto(User user) {
        return UserDto.builder()
                .id(user.getId())
                .name(user.getName())
                .build();
    }

    public static ItemRequest toItemRequest(ItemRequestDto itemRequestDto, User user) {
        return ItemRequest.builder()
                .id(itemRequestDto.getId())
                .description(itemRequestDto.getDescription())
                .requester(itemRequestDto.getRequester())
                .created(itemRequestDto.getCreated())
                .build();
    }
}

