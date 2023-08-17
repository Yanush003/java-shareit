package ru.practicum.unit;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.data.domain.Page;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.PageRequest;
import ru.practicum.item.Item;
import ru.practicum.item.ItemRepositoryJpa;
import ru.practicum.request.ItemRequest;
import ru.practicum.request.ItemRequestDescriptionDto;
import ru.practicum.request.ItemRequestRepositoryJpa;
import ru.practicum.request.ItemRequestService;
import ru.practicum.user.User;
import ru.practicum.user.UserRepositoryJpa;

import java.util.Collections;
import java.util.Optional;

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
        when(userRepository.findById(anyLong())).thenReturn(Optional.of(user));

        ItemRequest itemRequest = mock(ItemRequest.class);
        when(itemRequestRepository.findById(anyLong())).thenReturn(Optional.of(itemRequest));
        when(itemRepository.findAllByRequest(any(ItemRequest.class))).thenReturn(Collections.emptyList());

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

}
