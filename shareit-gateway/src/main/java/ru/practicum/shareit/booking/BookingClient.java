package ru.practicum.shareit.booking;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.DefaultUriBuilderFactory;
import ru.practicum.shareit.client.BaseClient;
import ru.practicum.shareit.library.api.booking.BookingController;
import ru.practicum.shareit.library.api.booking.dto.CreateBookingDto;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.util.Map;

@RestController
@Validated
public class BookingClient extends BaseClient implements BookingController {
    private static final String API_PREFIX = "/bookings";

    @Autowired
    public BookingClient(@Value("${shareit-server.url}") String serverUrl, RestTemplateBuilder builder) {
        super(
                builder
                        .uriTemplateHandler(new DefaultUriBuilderFactory(serverUrl + API_PREFIX))
                        .requestFactory(HttpComponentsClientHttpRequestFactory::new)
                        .build()
        );
    }

    @Override
    public ResponseEntity<Object> createBookingRequest(
            @Valid @Positive long userId,
            @Valid CreateBookingDto createBookingDto) {
        return post("", userId, createBookingDto);
    }

    @Override
    public ResponseEntity<Object> approveBooking(
            @Valid @Positive long ownerId,
            @Valid @Positive long bookingId,
            boolean approved) {
        Map<String, Object> parameters = Map.of("approved", approved);
        return patch(String.format("/%d?approved={approved}", bookingId), ownerId, parameters, null);
    }

    @Override
    public ResponseEntity<Object> getBookingById(
            @Valid @Positive long userId,
            @Valid @Positive long bookingId) {
        return get(String.format("/%d", bookingId), userId);
    }

    @Override
    public ResponseEntity<Object> getUserBookings(
            @Valid @Positive long userId,
            String state,
            @Valid @PositiveOrZero int from,
            @Valid @Positive int size) {

        Map<String, Object> parameters = Map.of(
                "state", state,
                "from", from,
                "size", size
        );
        return get("?from={from}&size={size}&state={state}", userId, parameters);
    }

    @Override
    public ResponseEntity<Object> getOwnerBookings(
            @Valid @Positive long ownerId,
            String state,
            @Valid @PositiveOrZero int from,
            @Valid @Positive int size) {

        Map<String, Object> parameters = Map.of(
                "state", state,
                "from", from,
                "size", size
        );
        return get("/owner?from={from}&size={size}&state={state}", ownerId, parameters);
    }
}
