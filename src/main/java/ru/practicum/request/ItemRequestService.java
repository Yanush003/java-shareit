package ru.practicum.request;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.exception.NotFoundException;
import ru.practicum.user.User;
import ru.practicum.user.UserRepositoryJpa;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ItemRequestService {
    private final ItemRequestRepositoryJpa itemRequestRepositoryJpa;
    private final UserRepositoryJpa userRepositoryJpa;

    public ItemRequest addRequest(Long userId, String description) {
        User user = getUserById(userId);
        return null;
    }

    public List<ItemRequestWithAnswersDto> getYourListRequests(Long userId) {
        User user = getUserById(userId);
        return null;
    }

    public List<ItemRequestWithAnswersDto> getOtherListRequests(Long userId, Long from, Long size) {
        User user = getUserById(userId);
        return null;
    }

    public ItemRequestWithAnswersDto getItemRequest(Long userId, Long requestId) {
        User user = getUserById(userId);
        return null;
    }

    public User getUserById(Long userId) {
        return userRepositoryJpa.findById(userId)
                .orElseThrow(() -> new NotFoundException(""));
    }
}
