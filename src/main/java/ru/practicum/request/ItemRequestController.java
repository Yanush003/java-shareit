package ru.practicum.request;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/requests")
@RequiredArgsConstructor
public class ItemRequestController {
    private final ItemRequestService itemRequestService;

    @PostMapping
    public ItemRequestDto addRequest(@RequestHeader("X-Sharer-User-Id") Long userId,
                                     @RequestBody @Valid ItemRequestDescriptionDto description) {

        return itemRequestService.addRequest(userId, description);
    }

    @GetMapping
    public List<ItemRequestWithAnswersDto> getYourListRequests(@RequestHeader("X-Sharer-User-Id") Long userId) {
        return itemRequestService.getYourListRequests(userId);
    }

    @GetMapping("/all")
    public List<ItemRequestWithAnswersDto> getOtherListRequests(@RequestHeader("X-Sharer-User-Id") Long userId,
                                                                @RequestParam(name = "from", defaultValue = "0") Integer from,
                                                                @RequestParam(name = "size", defaultValue = "10") Integer size) {
        return itemRequestService.getOtherListRequests(userId, from, size);
    }

    @GetMapping("/{requestId}")
    public ItemRequestWithAnswersDto getItemRequest(@RequestHeader("X-Sharer-User-Id") Long userId,
                                                    @PathVariable("requestId") Long requestId) {
        return itemRequestService.getItemRequest(userId, requestId);
    }
}
