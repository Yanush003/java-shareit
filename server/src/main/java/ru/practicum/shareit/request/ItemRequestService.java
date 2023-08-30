package ru.practicum.shareit.request;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exception.BadRequestException;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.item.Item;
import ru.practicum.shareit.item.ItemMapper;
import ru.practicum.shareit.item.ItemRepositoryJpa;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.UserRepositoryJpa;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ItemRequestService {
    private final ItemRequestRepositoryJpa itemRequestRepository;
    private final UserRepositoryJpa userRepository;
    private final ItemRepositoryJpa itemRepository;
    private final ItemMapper itemMapper;
    private final ItemRequestMapper itemRequestMapper;

    public ItemRequestDto addRequest(Long userId, ItemRequestDescriptionDto description) {
        User user = getUser(userId);
        if (description.getDescription().isBlank()) {
            throw new BadRequestException("");
        }
        ItemRequest itemRequest = ItemRequest.builder()
                .requester(user)
                .description(description.getDescription())
                .build();
        return itemRequestMapper.toItemRequestDto(itemRequestRepository.save(itemRequest));
    }

    public List<ItemRequestWithAnswersDto> getYourListRequests(Long userId) {
        User user = getUser(userId);
        List<Item> itemList = itemRepository.findAll().stream()
                .filter(x -> x.getRequest() != null)
                .collect(Collectors.toList());
        return itemRequestRepository.findByRequester(user).stream()
                .map(x -> itemRequestMapper.toItemRequestWithAnswersDto(x,
                        itemList.stream().filter(y -> y.getRequest().getId().equals(x.getId()))
                                .map(itemMapper::toAnswerItemDtoWithRequestId)
                                .collect(Collectors.toList())))
                .collect(Collectors.toList());
    }

    public List<ItemRequestWithAnswersDto> getOtherListRequests(Long userId, Integer from, Integer size) {
        User user = getUser(userId);
        PageRequest pageRequest = PageRequest.of(from, size);
        List<Item> itemList = itemRepository.findAll();
        List<Item> content = itemRequestRepository.findByRequester(user, pageRequest).getContent();
        return content.stream().map(x -> itemRequestMapper.toItemRequestWithAnswersDto(x.getRequest(),
                        itemList.stream()
                                .filter(y -> y.getId().equals(x.getId()))
                                .map(itemMapper::toAnswerItemDtoWithRequestId)
                                .collect(Collectors.toList())))
                .collect(Collectors.toList());
    }

    public ItemRequestWithAnswersDto getItemRequest(Long userId, Long requestId) {
        getUser(userId);
        ItemRequest itemRequest = getItem(requestId);
        return itemRequestMapper.toItemRequestWithAnswersDto(itemRequest,
                itemRepository.findAllByRequest(itemRequest).stream()
                        .map(itemMapper::toAnswerItemDtoWithRequestId)
                        .collect(Collectors.toList())
        );
    }

    public ItemRequest getItem(Long itemRequestId) {
        return itemRequestRepository.findById(itemRequestId)
                .orElseThrow(() -> new NotFoundException(""));
    }

    public User getUser(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException(""));
    }
}
