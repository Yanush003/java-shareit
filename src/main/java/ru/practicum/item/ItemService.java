package ru.practicum.item;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.exception.NotFoundException;
import ru.practicum.exception.ValidateEmailException;
import ru.practicum.user.UserService;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ItemService {

    private final ItemRepositoryJpa itemRepository;
    private final UserService userService;

    public Item get(Long itemId) {
        return itemRepository.findById(itemId)
                .orElseThrow(() -> new NotFoundException("Item with id=" + itemId + " not exist"));
    }

    public ItemDto add(Long userId, ItemDto itemDto) {
        Item item = ItemMapper.toItem(itemDto);
        item.setOwner(userService.get(userId));
        if (item.getName() == null
                || item.getName().isEmpty()
                || item.getDescription() == null
                || item.getAvailable() == null) {
            throw new ValidateEmailException("");
        }
        Item item1 = itemRepository.save(item);
        return ItemMapper.toItemDto(item1);
    }

    public Item update(Long userId, Long itemId, ItemDto itemDto) {
        Item item = get(itemId);
        if (itemDto.getName() != null) {
            item.setName(itemDto.getName());
        }
        if (itemDto.getDescription() != null) {
            item.setDescription(itemDto.getDescription());
        }
        if (itemDto.getAvailable() != null) {
            item.setAvailable(itemDto.getAvailable());
        }
        return itemRepository.save(item);
    }

    public List<Item> search(Long userId, String text) {
        if (text.isEmpty()) {
            return new ArrayList<>();
        }
        return itemRepository.findAll()
                .stream()
                .filter(item -> item.getDescription().toLowerCase().contains(text.toLowerCase()) && item.getAvailable().equals(true))
                .collect(Collectors.toList());
    }

    public List<Item> getAll(Long userId) {
        return itemRepository.findAllByOwner(userService.get(userId));
    }

    public Item createComment(Long itemId, String comment) {
        return null;
    }
}
