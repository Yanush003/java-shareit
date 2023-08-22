package ru.practicum.mapper;

import org.junit.jupiter.api.Test;
import ru.practicum.request.*;
import ru.practicum.user.User;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ItemRequestMapperTest {

    private final ItemRequestMapper itemRequestMapper = new ItemRequestMapper();


    @Test
    public void testToItemRequestDto() {
        ItemRequest itemRequest = createTestItemRequest();
        ItemRequestDto itemRequestDto = itemRequestMapper.toItemRequestDto(itemRequest);
        assertEquals(itemRequest.getId(), itemRequestDto.getId());
        assertEquals(itemRequest.getDescription(), itemRequestDto.getDescription());
        assertEquals(itemRequest.getRequester(), itemRequestDto.getRequester());
        assertEquals(itemRequest.getCreated(), itemRequestDto.getCreated());
    }

    @Test
    public void testToItemRequestWithAnswersDto() {
        ItemRequest itemRequest = createTestItemRequest();
        List<AnswerDto> answerDtos = createTestAnswerDtos();
        ItemRequestWithAnswersDto requestWithAnswersDto = itemRequestMapper.toItemRequestWithAnswersDto(itemRequest, answerDtos);
        assertEquals(itemRequest.getId(), requestWithAnswersDto.getId());
        assertEquals(itemRequest.getDescription(), requestWithAnswersDto.getDescription());
        assertEquals(itemRequest.getCreated(), requestWithAnswersDto.getCreated());
        assertEquals(answerDtos, requestWithAnswersDto.getItems());
    }

    private ItemRequest createTestItemRequest() {
        ItemRequest itemRequest = new ItemRequest();
        itemRequest.setId(1L);
        itemRequest.setDescription("Test Description");
        itemRequest.setRequester(new User());
        itemRequest.setCreated(LocalDateTime.now());
        return itemRequest;
    }

    private List<AnswerDto> createTestAnswerDtos() {
        List<AnswerDto> answerDtos = new ArrayList<>();
        answerDtos.add(AnswerDto.builder().id(1L).description("Answer 1").build());
        answerDtos.add(AnswerDto.builder().id(2L).description("Answer 2").build());
        return answerDtos;
    }
}
