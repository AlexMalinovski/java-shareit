package ru.practicum.shareit.booking;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.CreateBookingDto;
import ru.practicum.shareit.booking.enums.StateFilter;
import ru.practicum.shareit.booking.mapper.BookingMapper;
import ru.practicum.shareit.booking.mapper.EnumMapper;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.service.BookingService;
import ru.practicum.shareit.exception.BadRequestParamException;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.user.model.User;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/bookings")
public class BookingController {

    private final EnumMapper enumMapper;
    private final BookingMapper bookingMapper;
    private final BookingService bookingService;

    /**
     * Добавление нового запроса на бронирование. Запрос может быть создан любым пользователем, а затем подтверждён владельцем вещи.
     * Эндпоинт — POST /bookings.
     * После создания запрос находится в статусе WAITING — «ожидает подтверждения».
     * @param userId id пользователя
     * @param createBookingDto CreateBookingDto
     * @return BookingDto
     */
    @PostMapping
    public ResponseEntity<BookingDto> createBookingRequest(@RequestHeader("X-Sharer-User-Id") @Valid @Positive long userId,
                                                 @RequestBody @Valid CreateBookingDto createBookingDto) {
        Booking booking = bookingMapper.mapCreateBookingDtoToBooking(createBookingDto)
                .toBuilder()
                .booker(User.builder().id(userId).build())
                .build();
        final Booking createdBooking = bookingService.createBookingRequest(booking);
        return ResponseEntity.ok(bookingMapper.mapBookingToBookingDto(createdBooking));
    }

    /**
     * Подтверждение или отклонение запроса на бронирование. Может быть выполнено только владельцем вещи.
     * Затем статус бронирования становится либо APPROVED, либо REJECTED.
     * Эндпоинт — PATCH /bookings/{bookingId}?approved={approved},
     * параметр approved может принимать значения true или false.
     * @param ownerId id владельца вещи
     * @param bookingId id бронирования
     * @param approved подтверждение/отклонение бронирования
     * @return BookingDto
     */
    @PatchMapping("/{bookingId}")
    public ResponseEntity<BookingDto> approveBooking(@RequestHeader("X-Sharer-User-Id") @Valid @Positive long ownerId,
                                                     @PathVariable @Valid @Positive long bookingId,
                                                     @RequestParam boolean approved) {
        final Booking booking = bookingService.checkOwnerAndApproveBooking(bookingId, ownerId, approved);
        return ResponseEntity.ok(bookingMapper.mapBookingToBookingDto(booking));
    }

    /**
     * Получение данных о конкретном бронировании (включая его статус). Может быть выполнено либо автором бронирования,
     * либо владельцем вещи, к которой относится бронирование.
     * Эндпоинт — GET /bookings/{bookingId}
     * @param userId id пользователя
     * @param bookingId id бронирования
     * @return BookingDto
     */
    @GetMapping("/{bookingId}")
    public ResponseEntity<BookingDto> getBookingById(@RequestHeader("X-Sharer-User-Id") @Valid @Positive long userId,
                                                     @PathVariable @Valid @Positive long bookingId) {
        final Booking booking = bookingService.getBookingById(bookingId, userId)
                .orElseThrow(() -> new NotFoundException(String.format("Не найдено бронирование с id=%d.", bookingId)));
        return ResponseEntity.ok(bookingMapper.mapBookingToBookingDto(booking));

    }

    /**
     * Получение списка всех бронирований текущего пользователя.
     * Эндпоинт — GET /bookings?state={state}.
     * Параметр state необязательный и по умолчанию равен ALL (англ. «все»).
     * Также он может принимать значения CURRENT («текущие»), PAST («завершённые»), FUTURE («будущие»),
     * WAITING («ожидающие подтверждения»), REJECTED (англ. «отклонённые»).
     * @param userId id пользователя
     * @param state состояние бронирования
     * @return List<BookingDto> отсортированный по дате от более новых к более старым
     */
    @GetMapping
    public ResponseEntity<List<BookingDto>> getUserBookings(
            @RequestHeader("X-Sharer-User-Id") @Valid @Positive long userId,
            @RequestParam(required = false, defaultValue = "ALL") String state,
            @RequestParam(required = false, defaultValue = "0") @Valid @PositiveOrZero int from,
            @RequestParam(required = false, defaultValue = "20") @Valid @Positive int size) {

        StateFilter stateFilter = enumMapper.mapStringToStateFilter(state);
        if (stateFilter == StateFilter.UNSUPPORTED) {
            throw new BadRequestParamException(String.format("Unknown state: %s", state));
        }
        List<BookingDto> listDto = bookingMapper.mapBookingToBookingDto(
                bookingService.getUserBookings(stateFilter, userId, from, size));

        return ResponseEntity.ok(listDto);
    }

    /**
     * Получение списка бронирований для всех вещей текущего пользователя.
     * Эндпоинт — GET /bookings/owner?state={state}.
     * Этот запрос имеет смысл для владельца хотя бы одной вещи.
     * Параметр state необязательный и по умолчанию равен ALL (англ. «все»).
     * Также он может принимать значения CURRENT («текущие»), PAST («завершённые»), FUTURE («будущие»),
     * WAITING («ожидающие подтверждения»), REJECTED (англ. «отклонённые»).
     * @param ownerId id владельца вещи
     * @param state состояние бронирования
     * @return List<BookingDto>
     */
    @GetMapping("/owner")
    public ResponseEntity<List<BookingDto>> getOwnerBookings(
            @RequestHeader("X-Sharer-User-Id") @Valid @Positive long ownerId,
            @RequestParam(required = false, defaultValue = "ALL") String state,
            @RequestParam(required = false, defaultValue = "0") @Valid @PositiveOrZero int from,
            @RequestParam(required = false, defaultValue = "20") @Valid @Positive int size) {

        StateFilter stateFilter = enumMapper.mapStringToStateFilter(state);
        if (stateFilter == StateFilter.UNSUPPORTED) {
            throw new BadRequestParamException(String.format("Unknown state: %s", state));
        }
        List<BookingDto> listDto = bookingMapper.mapBookingToBookingDto(
                bookingService.getOwnerBookings(stateFilter, ownerId, from, size));

        return ResponseEntity.ok(listDto);
    }
}
