package ru.practicum.request;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import ru.practicum.exception.BadRequestException;
import ru.practicum.exception.NotFoundException;
import ru.practicum.item.ItemMapper;
import ru.practicum.item.ItemRepositoryJpa;
import ru.practicum.user.User;
import ru.practicum.user.UserService;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ItemRequestService {
    private final ItemRequestRepositoryJpa itemRequestRepository;
    private final UserService userService;
    private final ItemRepositoryJpa itemRepository;

    public ItemRequestDto addRequest(Long userId, ItemRequestDescriptionDto description) {
        User user = userService.get(userId);
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
        User user = userService.get(userId);
        //1. получить список реквестов
        //получить список айди из реквеста
        //2. получить вещи по реквестам - sql запрос SELECT a FROM Item a WHERE a.requestId IN (:requestsIds - список)

        //3. распределить вещи по реквестам в зависимости от айди
        //а)
        return itemRequestRepository.findByRequester(user).stream()
                .map(x -> ItemRequestMapper.toItemRequestWithAnswersDto(x,
                        itemRepository.findAllByRequest(x).stream()
                                .map(ItemMapper::toAnswerItemDtoWithRequestId)
                                .collect(Collectors.toList())))
                .collect(Collectors.toList());
    }

    public List<ItemRequestWithAnswersDto> getOtherListRequests(Long userId, Integer from, Integer size) {
        User user = userService.get(userId);
        PageRequest pageRequest = PageRequest.of(from, size);
        List<AnswerDto> answerDtos = new ArrayList<>();
        List<ItemRequest> content = itemRequestRepository.findByRequester(user, pageRequest).getContent();
        List<ItemRequestWithAnswersDto> collect = content.stream().map(
                x->ItemRequestMapper.
                )
                .collect(Collectors.toList());
        return collect;

    }

    public ItemRequestWithAnswersDto getItemRequest(Long userId, Long requestId) {
        User user = userService.get(userId);
        ItemRequest itemRequest = get(requestId);

        return ItemRequestMapper.toItemRequestWithAnswersDto(itemRequest,itemRepository.findAllByRequest(itemRequest).stream()
                .map(ItemMapper::toAnswerItemDtoWithRequestId)
                .collect(Collectors.toList()));
    }

    public ItemRequest get(Long itemRequestId) {
        return itemRequestRepository.findById(itemRequestId)
                .orElseThrow(() -> new NotFoundException(""));
    }

}
