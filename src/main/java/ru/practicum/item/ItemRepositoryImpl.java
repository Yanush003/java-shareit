package ru.practicum.item;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import ru.practicum.exception.NotFoundException;
import ru.practicum.exception.ValidateEmailException;
import ru.practicum.user.User;
import ru.practicum.user.UserRepositoryImpl;

import java.util.*;
import java.util.concurrent.atomic.AtomicLong;

@Repository
@RequiredArgsConstructor
public class ItemRepositoryImpl implements ItemRepository {

    Map<Long, List<Item>> items = new HashMap<>();

    private AtomicLong idCounter = new AtomicLong();

    @Override
    public Item get(Long itemId) {
        Optional<Item> first = items.values().stream()
                .flatMap(x -> x.stream().parallel())
                .filter(x -> x.getId().equals(itemId))
                .findFirst();
        if (first.isEmpty()) throw new NotFoundException("");
        return first.get();
    }

    public Item add(Long userId, Item item, User user) {
        List<Item> itemsUser = items.computeIfAbsent(userId, key -> new ArrayList<>());
        item.setId(idCounter.incrementAndGet());
        item.setOwner(user);
        itemsUser.add(item);
        return item;
    }

    @Override
    public Item update(Long userId, Long itemId, Item item) {
        List<Item> itemsUser;
        if (items.containsKey(userId)) {
            itemsUser = items.get(userId);
        } else {
            throw new NotFoundException("Передан пустой аргумент!");
        }
        Optional<Item> first = itemsUser.stream().filter(x -> x.getId().equals(itemId)).findFirst();
        if (first.isEmpty()){
            throw new NotFoundException("");
        }
        Item itemInList = first.get();
        if (item.getName() != null) {
            itemInList.setName(item.getName());
        }
        if (item.getDescription() != null) {
            itemInList.setDescription(item.getDescription());
        }
        if (item.getAvailable() != null) {
            itemInList.setAvailable(item.getAvailable());
        }
        itemsUser.remove(itemsUser.stream().filter(x -> x.getId().equals(itemId)).findFirst().get());
        itemsUser.add(itemInList);
        items.put(userId, itemsUser);
        return itemInList;
    }

    @Override
    public List<Item> search(Long userId, String text) {
        if (text.isEmpty()) {
            return new ArrayList<>();
        }
        List<Item> collect = items.values()
                .stream()
                .flatMap(Collection::stream)
                .filter(item -> item.getDescription().toLowerCase().contains(text.toLowerCase()) && item.getAvailable().equals(true))
                .toList();
        return collect;
    }

    public List<Item> getAll(Long userId) {
        if (items.get(userId) == null) {
            throw new NotFoundException("");
        }
        return items.get(userId);
    }
}
