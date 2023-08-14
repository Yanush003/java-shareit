package ru.practicum.request;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/requests")
@RequiredArgsConstructor
public class ItemRequestController {
    private final ItemRequestService itemRequestService;

    // добавить новый запрос вещи. Основная часть запроса — текст запроса, где пользователь описывает,
    // какая именно вещь ему нужна.
    @PostMapping
    public ItemRequestDto addRequest(@RequestHeader("X-Sharer-User-Id") Long userId,
                                     @RequestBody @Valid ItemRequestDescriptionDto description) {

        return itemRequestService.addRequest(userId, description);
    }

    // получить список своих запросов вместе с данными об ответах на них. Для каждого запроса должны указываться описание,
    // дата и время создания и список ответов в формате: id вещи, название, её описание description,
    // а также requestId запроса и признак доступности вещи available. Так в дальнейшем, используя указанные id вещей,
    // можно будет получить подробную информацию о каждой вещи.
    // Запросы должны возвращаться в отсортированном порядке от более новых к более старым.
    @GetMapping
    public List<ItemRequestWithAnswersDto> getYourListRequests(@RequestHeader("X-Sharer-User-Id") Long userId) {
        return itemRequestService.getYourListRequests(userId);
    }

    //получить список запросов, созданных другими пользователями.
    // С помощью этого эндпоинта пользователи смогут просматривать существующие запросы, на которые они могли бы ответить.
    // Запросы сортируются по дате создания: от более новых к более старым. Результаты должны возвращаться постранично.
    // Для этого нужно передать два параметра:
    // from — индекс первого элемента, начиная с 0, и size — количество элементов для отображения.
    @GetMapping("/all")
    public List<ItemRequestWithAnswersDto> getOtherListRequests(@RequestHeader("X-Sharer-User-Id") Long userId,
                                                  @RequestParam(name = "from", defaultValue = "0") Integer from,
                                                  @RequestParam(name = "size", defaultValue = "10") Integer size) {
        return itemRequestService.getOtherListRequests(userId, from, size);
    }

    //получить данные об одном конкретном запросе вместе с данными об ответах на него в том же формате,
    // что и в эндпоинте GET /requests.
    // Посмотреть данные об отдельном запросе может любой пользователь.
    @GetMapping("/{requestId}")
    public ItemRequestWithAnswersDto getItemRequest(@RequestHeader("X-Sharer-User-Id") Long userId,
                                                    @PathVariable("requestId") Long requestId) {
        return itemRequestService.getItemRequest(userId, requestId);
    }

}
