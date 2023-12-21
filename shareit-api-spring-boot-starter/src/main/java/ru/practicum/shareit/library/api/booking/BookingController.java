package ru.practicum.shareit.library.api.booking;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import ru.practicum.shareit.library.api.booking.dto.CreateBookingDto;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;

@RequestMapping(path = "/bookings")
public interface BookingController {

    /**
     * Добавление нового запроса на бронирование. Запрос может быть создан любым пользователем, а затем подтверждён владельцем вещи.
     * Эндпоинт — POST /bookings.
     * После создания запрос находится в статусе WAITING — «ожидает подтверждения».
     *
     * @param userId           id пользователя
     * @param createBookingDto CreateBookingDto
     * @return BookingDto
     */
    @PostMapping
    ResponseEntity<Object> createBookingRequest(@RequestHeader("X-Sharer-User-Id") @Valid @Positive long userId,
                                                    @RequestBody @Valid CreateBookingDto createBookingDto);

    /**
     * Подтверждение или отклонение запроса на бронирование. Может быть выполнено только владельцем вещи.
     * Затем статус бронирования становится либо APPROVED, либо REJECTED.
     * Эндпоинт — PATCH /bookings/{bookingId}?approved={approved},
     * параметр approved может принимать значения true или false.
     *
     * @param ownerId   id владельца вещи
     * @param bookingId id бронирования
     * @param approved  подтверждение/отклонение бронирования
     * @return BookingDto
     */
    @PatchMapping("/{bookingId}")
    ResponseEntity<Object> approveBooking(@RequestHeader("X-Sharer-User-Id") @Valid @Positive long ownerId,
                                              @PathVariable @Valid @Positive long bookingId,
                                              @RequestParam boolean approved);

    /**
     * Получение данных о конкретном бронировании (включая его статус). Может быть выполнено либо автором бронирования,
     * либо владельцем вещи, к которой относится бронирование.
     * Эндпоинт — GET /bookings/{bookingId}
     *
     * @param userId    id пользователя
     * @param bookingId id бронирования
     * @return BookingDto
     */
    @GetMapping("/{bookingId}")
    ResponseEntity<Object> getBookingById(@RequestHeader("X-Sharer-User-Id") @Valid @Positive long userId,
                                              @PathVariable @Valid @Positive long bookingId);

    /**
     * Получение списка всех бронирований текущего пользователя.
     * Эндпоинт — GET /bookings?state={state}.
     * Параметр state необязательный и по умолчанию равен ALL (англ. «все»).
     * Также он может принимать значения CURRENT («текущие»), PAST («завершённые»), FUTURE («будущие»),
     * WAITING («ожидающие подтверждения»), REJECTED (англ. «отклонённые»).
     *
     * @param userId id пользователя
     * @param state  состояние бронирования
     * @return List<BookingDto> отсортированный по дате от более новых к более старым
     */
    @GetMapping
    ResponseEntity<Object> getUserBookings(
            @RequestHeader("X-Sharer-User-Id") @Valid @Positive long userId,
            @RequestParam(required = false, defaultValue = "ALL") String state,
            @RequestParam(required = false, defaultValue = "0") @Valid @PositiveOrZero int from,
            @RequestParam(required = false, defaultValue = "20") @Valid @Positive int size);

    /**
     * Получение списка бронирований для всех вещей текущего пользователя.
     * Эндпоинт — GET /bookings/owner?state={state}.
     * Этот запрос имеет смысл для владельца хотя бы одной вещи.
     * Параметр state необязательный и по умолчанию равен ALL (англ. «все»).
     * Также он может принимать значения CURRENT («текущие»), PAST («завершённые»), FUTURE («будущие»),
     * WAITING («ожидающие подтверждения»), REJECTED (англ. «отклонённые»).
     *
     * @param ownerId id владельца вещи
     * @param state   состояние бронирования
     * @return List<BookingDto>
     */
    @GetMapping("/owner")
    ResponseEntity<Object> getOwnerBookings(
            @RequestHeader("X-Sharer-User-Id") @Valid @Positive long ownerId,
            @RequestParam(required = false, defaultValue = "ALL") String state,
            @RequestParam(required = false, defaultValue = "0") @Valid @PositiveOrZero int from,
            @RequestParam(required = false, defaultValue = "20") @Valid @Positive int size);
}
