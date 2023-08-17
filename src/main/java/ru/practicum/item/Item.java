package ru.practicum.item;

import lombok.*;
import ru.practicum.request.ItemRequest;
import ru.practicum.user.User;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@Entity
@Table(name = "items")
public class Item {

    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    private Long id;
    @NotBlank
    private String name;
    @NotBlank
    private String description;
    @NotNull
    private Boolean available; //— статус о том, доступна или нет вещь для аренды; Статус должен проставлять владелец.
    @NotNull
    @ManyToOne
    @JoinColumn(name = "owner_id")
    private User owner; //— владелец вещи;
    @OneToOne
    @JoinColumn(name = "request_id")
    private ItemRequest request; // если вещь была создана по запросу другого пользователя, то в этом
    // поле будет храниться ссылка на соответствующий запрос
}
