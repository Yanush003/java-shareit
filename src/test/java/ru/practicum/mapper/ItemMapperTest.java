package ru.practicum.mapper;

import org.junit.jupiter.api.Test;
import ru.practicum.item.Item;
import ru.practicum.item.ItemDto;
import ru.practicum.item.ItemMapper;
import ru.practicum.request.AnswerDto;
import ru.practicum.request.ItemRequest;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ItemMapperTest {

    private final ItemMapper itemMapper = new ItemMapper();

    @Test
    public void testToItemDto() {
        Item item = createTestItem();

        ItemDto itemDto = itemMapper.toItemDto(item);

        assertEquals(item.getId(), itemDto.getId());
        assertEquals(item.getName(), itemDto.getName());
        assertEquals(item.getDescription(), itemDto.getDescription());
        assertEquals(item.getRequest(), itemDto.getRequest());
        assertEquals(item.getAvailable(), itemDto.getAvailable());
    }

    @Test
    public void testToItem() {
        ItemDto itemDto = createTestItemDto();

        Item item = itemMapper.toItem(itemDto);

        assertEquals(itemDto.getId(), item.getId());
        assertEquals(itemDto.getName(), item.getName());
        assertEquals(itemDto.getDescription(), item.getDescription());
        assertEquals(itemDto.getAvailable(), item.getAvailable());
        assertEquals(itemDto.getOwner(), item.getOwner());
    }

    @Test
    public void testToAnswerItemDtoWithRequestId() {
        Item item = createTestItemWithRequest();

        AnswerDto answerDto = itemMapper.toAnswerItemDtoWithRequestId(item);

        assertEquals(item.getId(), answerDto.getId());
        assertEquals(item.getName(), answerDto.getName());
        assertEquals(item.getDescription(), answerDto.getDescription());
        assertEquals(item.getAvailable(), answerDto.getAvailable());
        assertEquals(item.getRequest().getId(), answerDto.getRequestId());
    }

    private Item createTestItem() {
        Item item = new Item();
        item.setId(1L);
        item.setName("Test Item");
        item.setDescription("Test Description");
        item.setAvailable(true);
        return item;
    }

    private ItemDto createTestItemDto() {
        return ItemDto.builder()
                .id(1L)
                .name("Test Item")
                .description("Test Description")
                .available(true)
                .build();
    }

    private Item createTestItemWithRequest() {
        Item item = new Item();
        item.setId(1L);
        item.setName("Test Item");
        item.setDescription("Test Description");
        item.setAvailable(true);
        ItemRequest request = new ItemRequest();
        request.setId(1L);
        item.setRequest(request);
        return item;
    }
}
