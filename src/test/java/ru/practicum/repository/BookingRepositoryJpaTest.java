package ru.practicum.repository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import ru.practicum.booking.Booking;
import ru.practicum.booking.BookingRepositoryJpa;
import ru.practicum.booking.Status;
import ru.practicum.item.Item;
import ru.practicum.user.User;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
public class BookingRepositoryJpaTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private BookingRepositoryJpa bookingRepositoryJpa;

    @Test
    public void testFindAllByBookerId() {
        User booker = User.builder()
                .name("Test User")
                .email("test@example.com")
                .build();
        entityManager.persist(booker);
        Item item = Item.builder()
                .name("Тестовый предмет")
                .available(true)
                .owner(booker)
                .description("Тестовое описание")
                .build();
        entityManager.persist(item);
        Booking booking = Booking.builder()
                .booker(booker)
                .item(item)
                .status(Status.APPROVED)
                .build();
        entityManager.persist(booking);
        Page<Booking> results = bookingRepositoryJpa.findAllByBookerId(booker.getId(), PageRequest.of(0, 10));
        assertThat(results.getContent()).hasSize(1);
        assertThat(results.getContent().get(0).getBooker()).isEqualTo(booker);
    }

    @Test
    public void testFindAllByBooker_Id() {
        User booker = User.builder()
                .name("Test User")
                .email("test@example.com")
                .build();
        entityManager.persist(booker);
        Item item = Item.builder()
                .name("Тестовый предмет")
                .available(true)
                .owner(booker)
                .description("Тестовое описание")
                .build();
        entityManager.persist(item);
        Booking booking = Booking.builder()
                .booker(booker)
                .item(item)
                .status(Status.APPROVED)
                .build();
        entityManager.persist(booking);
        List<Booking> results = bookingRepositoryJpa.findAllByBooker_Id(booker.getId());
        assertThat(results).hasSize(1);
        assertThat(results.get(0).getBooker()).isEqualTo(booker);
    }

    @Test
    public void testFindAllByItem_OwnerId() {
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
        Booking booking = Booking.builder()
                .booker(owner)
                .item(item)
                .status(Status.APPROVED)
                .build();
        entityManager.persist(booking);
        Page<Booking> results = bookingRepositoryJpa.findAllByItem_OwnerId(owner.getId(), PageRequest.of(0, 10));
        assertThat(results.getContent()).hasSize(1);
        assertThat(results.getContent().get(0).getItem().getOwner()).isEqualTo(owner);
    }

    @Test
    public void testFindAllByItemIdAndBookerId() {
        User booker = User.builder()
                .name("Test User")
                .email("test@example.com")
                .build();
        entityManager.persist(booker);
        Item item = Item.builder()
                .name("Тестовый предмет")
                .available(true)
                .owner(booker)
                .description("Тестовое описание")
                .build();
        entityManager.persist(item);
        Booking booking = Booking.builder()
                .booker(booker)
                .item(item)
                .status(Status.APPROVED)
                .build();
        entityManager.persist(booking);
        List<Booking> results = bookingRepositoryJpa.findAllByItemIdAndBookerId(item.getId(), booker.getId());
        assertThat(results).hasSize(1);
        assertThat(results.get(0).getItem()).isEqualTo(item);
        assertThat(results.get(0).getBooker()).isEqualTo(booker);
    }

    @Test
    public void testFindAllByItemId() {
        User booker = User.builder()
                .name("Test User")
                .email("test@example.com")
                .build();
        entityManager.persist(booker);
        Item item = Item.builder()
                .name("Тестовый предмет")
                .available(true)
                .owner(booker)
                .description("Тестовое описание")
                .build();
        entityManager.persist(item);
        Booking booking = Booking.builder()
                .booker(booker)
                .item(item)
                .status(Status.APPROVED)
                .build();
        entityManager.persist(booking);
        List<Booking> results = bookingRepositoryJpa.findAllByItemId(item.getId());
        assertThat(results).hasSize(1);
        assertThat(results.get(0).getItem()).isEqualTo(item);
    }
}

