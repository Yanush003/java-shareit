package ru.practicum.repository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import ru.practicum.item.Item;
import ru.practicum.request.ItemRequest;
import ru.practicum.request.ItemRequestRepositoryJpa;
import ru.practicum.user.User;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
public class ItemRequestRepositoryJpaTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private ItemRequestRepositoryJpa itemRequestRepositoryJpa;

    @Test
    public void testFindByRequester() {
        User requester = User.builder()
                .name("Test User")
                .email("test@example.com")
                .build();
        entityManager.persist(requester);

        ItemRequest request1 = new ItemRequest();
        request1.setRequester(requester);
        request1.setDescription("Test Request 1");
        request1.setCreated(LocalDateTime.now());
        entityManager.persist(request1);

        ItemRequest request2 = new ItemRequest();
        request2.setRequester(requester);
        request2.setDescription("Test Request 2");
        request2.setCreated(LocalDateTime.now());
        entityManager.persist(request2);
        List<ItemRequest> results = itemRequestRepositoryJpa.findByRequester(requester);
        assertThat(results).hasSize(2);
        assertThat(results).containsExactlyInAnyOrder(request1, request2);
    }

    @Test
    public void testFindByRequesterWithPagination() {
        User requester = User.builder()
                .name("Test User")
                .email("test@example.com")
                .build();
        entityManager.persist(requester);

        ItemRequest request = new ItemRequest();
        request.setRequester(requester);
        request.setDescription("Test Request for Pagination");
        request.setCreated(LocalDateTime.now());
        entityManager.persist(request);
        Item item = Item.builder()
                .name("Тестовый предмет")
                .available(true)
                .owner(requester)
                .description("Тестовое описание")
                .request(request)
                .build();
        entityManager.persist(item);
        Pageable pageable = PageRequest.of(0, 10);
        Page<Item> results = itemRequestRepositoryJpa.findByRequester(requester, pageable);
        assertThat(results.getContent()).hasSize(1);
        assertThat(results.getContent().get(0).getRequest()).isEqualTo(request);
    }
}
