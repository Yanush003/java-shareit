package ru.practicum.unit;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import ru.practicum.exception.BadRequestException;
import ru.practicum.exception.NotFoundException;
import ru.practicum.item.Item;
import ru.practicum.item.ItemMapper;
import ru.practicum.item.ItemRepositoryJpa;
import ru.practicum.request.*;
import ru.practicum.user.User;
import ru.practicum.user.UserRepositoryJpa;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

public class ItemRequestServiceTest {

    @InjectMocks
    private ItemRequestService itemRequestService;

    @Mock
    private ItemRequestRepositoryJpa itemRequestRepository;

    @Mock
    private UserRepositoryJpa userRepository;

    @Mock
    private ItemRepositoryJpa itemRepository;

    @Mock
    private ItemRequestMapper itemRequestMapper;
    @Mock
    private ItemMapper itemMapper;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testAddRequest() {
        User user = mock(User.class);
        when(userRepository.findById(anyLong())).thenReturn(Optional.of(user));
        ItemRequest itemRequest = mock(ItemRequest.class);
        when(itemRequestRepository.save(any(ItemRequest.class))).thenReturn(itemRequest);
        when(itemRequestMapper.toItemRequestDto(any(ItemRequest.class))).thenReturn(any(ItemRequestDto.class));
        ItemRequestDescriptionDto itemRequestDescriptionDto = new ItemRequestDescriptionDto();
        itemRequestDescriptionDto.setDescription("Test Description");
        itemRequestService.addRequest(1L, itemRequestDescriptionDto);
        verify(userRepository).findById(1L);
        verify(itemRequestRepository).save(any(ItemRequest.class));
    }

    @Test
    public void testGetYourListRequests() {
        User user = mock(User.class);
        when(userRepository.findById(anyLong())).thenReturn(Optional.of(user));
        when(itemRequestRepository.findByRequester(user)).thenReturn(Collections.emptyList());
        itemRequestService.getYourListRequests(1L);
        verify(userRepository).findById(1L);
        verify(itemRepository).findAll();
        verify(itemRequestRepository).findByRequester(user);
    }

    @Test
    public void testGetOtherListRequests() {
        User user = mock(User.class);
        when(userRepository.findById(anyLong())).thenReturn(Optional.of(user));
        Page<Item> page = mock(Page.class);
        when(page.getContent()).thenReturn(Collections.emptyList());
        when(itemRequestRepository.findByRequester(any(User.class), any(PageRequest.class))).thenReturn(page);
        itemRequestService.getOtherListRequests(1L, 0, 10);
        verify(userRepository).findById(1L);
        verify(itemRequestRepository).findByRequester(user, PageRequest.of(0, 10));
        verify(itemRepository).findAll();
    }


    @Test
    public void testGetItemRequest() {
        User user = mock(User.class);
        Item item = createTestItemWithRequest();
        when(userRepository.findById(anyLong())).thenReturn(Optional.of(user));
        ItemRequest itemRequest = createTestItemRequest();
        when(itemRequestRepository.findById(anyLong())).thenReturn(Optional.of(itemRequest));
        when(itemRepository.findAllByRequest(any(ItemRequest.class))).thenReturn(Collections.emptyList());
        when(itemRequestMapper.toItemRequestWithAnswersDto(createTestItemRequest(), createTestAnswerDtos())).thenReturn(any(ItemRequestWithAnswersDto.class));
        when(itemMapper.toAnswerItemDtoWithRequestId(item)).thenReturn(any(AnswerDto.class));
        itemRequestService.getItemRequest(1L, 1L);
        verify(userRepository).findById(1L);
        verify(itemRequestRepository).findById(1L);
    }

    @Test
    public void testGetItem() {
        ItemRequest itemRequest = mock(ItemRequest.class);
        when(itemRequestRepository.findById(anyLong())).thenReturn(Optional.of(itemRequest));
        itemRequestService.getItem(1L);
        verify(itemRequestRepository).findById(1L);
    }

    @Test
    public void testGetUser() {
        User user = mock(User.class);
        when(userRepository.findById(anyLong())).thenReturn(Optional.of(user));
        itemRequestService.getUser(1L);
        verify(userRepository).findById(1L);
    }

    @Test
    public void testAddRequestWithBlankDescription() {
        User user = mock(User.class);
        when(userRepository.findById(anyLong())).thenReturn(Optional.of(user));
        ItemRequestDescriptionDto itemRequestDescriptionDto = new ItemRequestDescriptionDto();
        itemRequestDescriptionDto.setDescription("");
        assertThrows(BadRequestException.class, () -> itemRequestService.addRequest(1L, itemRequestDescriptionDto));
    }

    @Test
    public void testGetItemRequestNotFound() {
        when(userRepository.findById(anyLong())).thenReturn(Optional.of(mock(User.class)));
        when(itemRequestRepository.findById(anyLong())).thenReturn(Optional.empty());
        assertThrows(NotFoundException.class, () -> itemRequestService.getItemRequest(1L, 1L));
    }

    @Test
    public void testGetUserNotFound() {
        when(userRepository.findById(anyLong())).thenReturn(Optional.empty());
        assertThrows(NotFoundException.class, () -> itemRequestService.getUser(1L));
    }

    private List<AnswerDto> createTestAnswerDtos() {
        List<AnswerDto> answerDtos = new ArrayList<>();
        answerDtos.add(AnswerDto.builder().id(1L).description("Answer 1").build());
        answerDtos.add(AnswerDto.builder().id(2L).description("Answer 2").build());
        return answerDtos;
    }

    private ItemRequest createTestItemRequest() {
        ItemRequest itemRequest = new ItemRequest();
        itemRequest.setId(1L);
        itemRequest.setDescription("Test Description");
        itemRequest.setRequester(new User());
        itemRequest.setCreated(LocalDateTime.now());
        return itemRequest;
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
