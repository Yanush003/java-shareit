package ru.practicum.repository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import ru.practicum.item.Item;
import ru.practicum.item.ItemRepositoryJpa;
import ru.practicum.request.ItemRequest;
import ru.practicum.user.User;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
public class ItemRepositoryJpaTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private ItemRepositoryJpa itemRepositoryJpa;

    @Test
    public void testFindAllByOwner() {
        User owner = User.builder()
                .name("Test User")
                .email("test@example.com")
                .build();
        entityManager.persist(owner);
        Item item = Item.builder()
                .name("Тестовый предмет")
                .available(true)
                .owner(owner)
                .description("Тестовое описание")
                .build();
        entityManager.persist(item);
        List<Item> results = itemRepositoryJpa.findAllByOwner(owner);
        assertThat(results).hasSize(1);
        assertThat(results.get(0).getOwner()).isEqualTo(owner);
    }

    @Test
    public void testFindAllByRequest() {
        User owner = User.builder()
                .name("Test User")
                .email("test@example.com")
                .build();
        entityManager.persist(owner);
        ItemRequest itemRequest = ItemRequest.builder()
                .requester(owner)
                .build();
        entityManager.persist(itemRequest);
        Item item = Item.builder()
                .name("Тестовый предмет")
                .available(true)
                .owner(owner)
                .description("Тестовое описание")
                .request(itemRequest)
                .build();
        entityManager.persist(item);
        List<Item> results = itemRepositoryJpa.findAllByRequest(itemRequest);
        assertThat(results).hasSize(1);
        assertThat(results.get(0).getRequest()).isEqualTo(itemRequest);
    }
}

