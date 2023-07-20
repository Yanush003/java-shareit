package ru.practicum.item;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.servlet.annotation.HandlesTypes;
import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/items")
@RequiredArgsConstructor
public class ItemController {
    private final ItemService itemService;

    @GetMapping("/{itemId}")
    public Item get(@PathVariable("itemId") Long itemId) {
        return itemService.get(itemId);
    }

    @PostMapping
    public ItemDto create(@RequestHeader("X-Sharer-User-Id") Long userId,
                    @RequestBody ItemDto itemDto) {
        return itemService.add(userId, itemDto);
    }

    @PatchMapping("/{itemId}")
    public Item update(@RequestHeader("X-Sharer-User-Id") Long userId,
                       @PathVariable("itemId") Long itemId,
                       @RequestBody ItemDto itemDto) {
        return itemService.update(userId, itemId, itemDto);
    }

    @GetMapping("/search")
    public List<Item> search(@RequestHeader("X-Sharer-User-Id") Long userId,
                             @RequestParam("text") String text) {
        return itemService.search(userId, text);
    }

    @GetMapping
    public List<Item> getAllItem(@RequestHeader("X-Sharer-User-Id") Long userId){
        return itemService.getAll(userId);
    }
}
