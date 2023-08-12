package ru.practicum.request;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import ru.practicum.exception.BadRequestException;
import ru.practicum.exception.NotFoundException;
import ru.practicum.user.User;
import ru.practicum.user.UserService;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ItemRequestService {
    private final ItemRequestRepositoryJpa itemRequestRepository;
    private final UserService userService;

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
        List<AnswerDto> answerDtos = new ArrayList<>();
        return itemRequestRepository.findByRequester(user).stream().map(x -> ItemRequestMapper.toItemRequestWithAnswersDto(x, answerDtos)).collect(Collectors.toList());
    }

    public List<ItemRequest> getOtherListRequests(Long userId, Integer from, Integer size) {
        User user = userService.get(userId);
        PageRequest pageRequest = PageRequest.of(from, size);
        return itemRequestRepository.findByRequester(user, pageRequest).getContent();

    }

    public ItemRequestWithAnswersDto getItemRequest(Long userId, Long requestId) {

        return null;
    }

    public ItemRequest get(Long itemRequestId) {
        return itemRequestRepository.findById(itemRequestId)
                .orElseThrow(() -> new NotFoundException(""));
    }

}
