package ru.practicum.shareit.itemrequest;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.itemrequest.dto.ItemRequestDescriptionDto;

import javax.validation.Valid;

@RestController
@RequestMapping("/requests")
@RequiredArgsConstructor
public class ItemRequestController {
    private final ItemRequestClient itemRequestClient;

    @PostMapping
    public ResponseEntity<Object> addRequest(@RequestHeader("X-Sharer-User-Id") Long userId,
                                             @RequestBody @Valid ItemRequestDescriptionDto description) {
        return itemRequestClient.createRequest(userId, description);
    }

    @GetMapping
    public ResponseEntity<Object> getYourListRequests(@RequestHeader("X-Sharer-User-Id") Long userId) {
        return itemRequestClient.getYourListRequests(userId);
    }

    @GetMapping("/all")
    public ResponseEntity<Object> getOtherListRequests(@RequestHeader("X-Sharer-User-Id") Long userId,
                                                       @RequestParam(name = "from", defaultValue = "0") Integer from,
                                                       @RequestParam(name = "size", defaultValue = "10") Integer size) {
        return itemRequestClient.getOtherListRequests(userId, from, size);
    }

    @GetMapping("/{requestId}")
    public ResponseEntity<Object> getItemRequest(@RequestHeader("X-Sharer-User-Id") Long userId,
                                                 @PathVariable("requestId") Long requestId) {
        return itemRequestClient.getItemRequest(userId, requestId);
    }
}
