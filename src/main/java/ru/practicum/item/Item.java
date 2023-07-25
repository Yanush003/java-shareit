package ru.practicum.item;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.practicum.request.ItemRequest;
import ru.practicum.user.User;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name="items")
public class Item {
    @Column(name="id")
    @Id
    private Long id;
    @NotBlank
    @Column(name = "name")
    private String name;
    @Column (name = "description")
    private String description;
    @Column(name = "available")
    private Boolean available; //— статус о том, доступна или нет вещь для аренды; Статус должен проставлять владелец.
    @Transient
    @ManyToOne
    private User owner; //— владелец вещи;
    @Transient
    @OneToOne
    private ItemRequest request; // если вещь была создана по запросу другого пользователя, то в этом
    // поле будет храниться ссылка на соответствующий запрос
}
