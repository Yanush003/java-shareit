package ru.practicum.request;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import ru.practicum.exception.BadRequestException;
import ru.practicum.exception.NotFoundException;
import ru.practicum.item.Item;
import ru.practicum.item.ItemMapper;
import ru.practicum.item.ItemRepositoryJpa;
import ru.practicum.user.User;
import ru.practicum.user.UserRepositoryJpa;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ItemRequestService {
    private final ItemRequestRepositoryJpa itemRequestRepository;
    private final UserRepositoryJpa userRepository;
    private final ItemRepositoryJpa itemRepository;

    public ItemRequestDto addRequest(Long userId, ItemRequestDescriptionDto description) {
        User user = getUser(userId);
        if (description.getDescription().isBlank()) {
            throw new BadRequestException("");
        }
        ItemRequest itemRequest = ItemRequest.builder()
                .requester(user)
                .description(description.getDescription())
                .build();
        return ItemRequestMapper.toItemRequestDto(itemRequestRepository.save(itemRequest));
    }

    public List<ItemRequestWithAnswersDto> getYourListRequests(Long userId) {
        User user = getUser(userId);
        List<Item> itemList = itemRepository.findAll().stream()
                .filter(x -> x.getRequest() != null)
                .collect(Collectors.toList());
        return itemRequestRepository.findByRequester(user).stream()
                .map(x -> ItemRequestMapper.toItemRequestWithAnswersDto(x,
                        itemList.stream().filter(y -> y.getRequest().getId().equals(x.getId()))
                                .map(ItemMapper::toAnswerItemDtoWithRequestId)
                                .collect(Collectors.toList())))
                .collect(Collectors.toList());
    }

    public List<ItemRequestWithAnswersDto> getOtherListRequests(Long userId, Integer from, Integer size) {
        User user = getUser(userId);
        PageRequest pageRequest = PageRequest.of(from, size);
        List<Item> itemList = itemRepository.findAll();
        List<Item> content = itemRequestRepository.findByRequester(user, pageRequest).getContent();
        return content.stream().map(x -> ItemRequestMapper.toItemRequestWithAnswersDto(x.getRequest(),
                        itemList.stream()
                                .filter(y -> y.getId().equals(x.getId()))
                                .map(ItemMapper::toAnswerItemDtoWithRequestId)
                                .collect(Collectors.toList())))
                .collect(Collectors.toList());
    }

    public ItemRequestWithAnswersDto getItemRequest(Long userId, Long requestId) {
        getUser(userId);
        ItemRequest itemRequest = getItem(requestId);
        return ItemRequestMapper.toItemRequestWithAnswersDto(itemRequest,
                itemRepository.findAllByRequest(itemRequest).stream()
                        .map(ItemMapper::toAnswerItemDtoWithRequestId)
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
