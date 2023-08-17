package ru.practicum.repository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import ru.practicum.comment.Comment;
import ru.practicum.comment.CommentRepositoryJpa;
import ru.practicum.item.Item;
import ru.practicum.user.User;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

// Ваши собственные импорты моделей (User, Item,


@DataJpaTest
public class CommentRepositoryJpaTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private CommentRepositoryJpa commentRepositoryJpa;

    @Test
    public void testFindAllByItemId() {
        User user = User.builder()
                .name("Test User")
                .email("test@example.com")
                .build();
        entityManager.persist(user);

        Item item = Item.builder()
                .name("Тестовый предмет")
                .available(true)
                .owner(user)
                .description("Тестовое описание")
                .build();
        entityManager.persist(item);

        Comment comment = Comment.builder()
                .item(item)
                .author(user)
                .created(LocalDateTime.now())
                .text("Тестовый комментарий")
                .build();
        entityManager.persist(comment);

        List<Comment> results = commentRepositoryJpa.findAllByItemId(item.getId());
        assertThat(results).hasSize(1);
        assertThat(results.get(0).getItem()).isEqualTo(item);
        assertThat(results.get(0).getAuthor()).isEqualTo(user);
        assertThat(results.get(0).getText()).isEqualTo("Тестовый комментарий");
    }

    @Test
    public void testFindAllByNonExistentItemId() {
        Long nonExistentId = 99999L;
        List<Comment> results = commentRepositoryJpa.findAllByItemId(nonExistentId);
        assertThat(results).isEmpty();
    }
}
