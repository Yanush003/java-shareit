package ru.practicum.item;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.exception.ValidateEmailException;
import ru.practicum.user.User;
import ru.practicum.user.UserRepositoryImpl;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ItemService {

    private final ItemRepositoryImpl itemRepository;
    private final UserRepositoryImpl userRepository;

    public Item get(Long userId) {
        return itemRepository.get(userId);
    }

    public ItemDto add(Long userId, ItemDto itemDto) {
        Item item = ItemMapper.toItem(itemDto);
        if (item.getName() == null
                || item.getName().isEmpty()
                || item.getDescription() == null
                || item.getAvailable() == null)
        {
            throw new ValidateEmailException("");
        }
        User user = userRepository.get(userId);
        Item item1 = itemRepository.add(userId, item, user);
        return ItemMapper.toItemDto(item1);
    }

    public Item update(Long userId, Long itemId, ItemDto itemDto) {
        Item item = ItemMapper.toItem(itemDto);
        return itemRepository.update(userId, itemId, item);
    }

    public List<Item> search(Long userId,String text) {
        return itemRepository.search(userId, text);
    }

    public List<Item> getAll(Long userId) {
        return itemRepository.getAll(userId);
    }
}
