package ru.practicum.item;

import ru.practicum.request.ItemRequest;

public class ItemMapper {
    public static ItemDto toItemDto(Item item) {
        return ItemDto.builder()
                .id((item.getId() == null) ? null : item.getId())
                .name((item.getName() == null) ? null : item.getName())
                .description((item.getDescription() == null) ? null : item.getDescription())
                .available((item.getAvailable() == null) ? null : item.getAvailable())
                .owner((item.getOwner() == null) ? null : item.getOwner())
                .request(ItemRequest.builder().id((item.getRequest() == null) ? null : item.getRequest().getId()).build())
                .build();
    }

    public static Item toItem(ItemDto itemDto) {
        return Item.builder()
                .id((itemDto.getId() == null) ? null : itemDto.getId())
                .name((itemDto.getName() == null) ? null : itemDto.getName())
                .description((itemDto.getDescription() == null) ? null : itemDto.getDescription())
                .available((itemDto.getAvailable() == null) ? null : itemDto.getAvailable())
                .owner((itemDto.getOwner() == null) ? null : itemDto.getOwner())
                .request(ItemRequest.builder().id((itemDto.getRequest() == null) ? null : itemDto.getRequest().getId()).build())
                .build();
    }
}
