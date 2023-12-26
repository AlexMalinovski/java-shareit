package ru.practicum.shareit.gateway.booking.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.util.DefaultUriBuilderFactory;
import ru.practicum.shareit.gateway.client.BaseClient;
import ru.practicum.shareit.library.api.booking.dto.CreateBookingDto;
import ru.practicum.shareit.library.api.exception.BadRequestParamException;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
@Validated
public class BookingClientImpl extends BaseClient implements BookingClient {
    private static final String API_PREFIX = "/bookings";

    private static final Pattern STATE_FILTER_PATTERN =
            Pattern.compile("^(ALL|CURRENT|PAST|FUTURE|WAITING|REJECTED)$", Pattern.CASE_INSENSITIVE);

    @Autowired
    public BookingClientImpl(@Value("${shareit-server.url}") String serverUrl, RestTemplateBuilder builder) {
        super(
                builder
                        .uriTemplateHandler(new DefaultUriBuilderFactory(serverUrl + API_PREFIX))
                        .requestFactory(HttpComponentsClientHttpRequestFactory::new)
                        .build()
        );
    }

    @Override
    public ResponseEntity<Object> createBookingRequest(
            @Valid @Positive long userId, @Valid CreateBookingDto createBookingDto) {

        return post("", userId, createBookingDto);
    }

    @Override
    public ResponseEntity<Object> approveBooking(
            @Valid @Positive long ownerId, @Valid @Positive long bookingId, boolean approved) {

        Map<String, Object> parameters = Map.of("approved", approved);
        return patch(String.format("/%d?approved={approved}", bookingId), ownerId, parameters, null);
    }

    @Override
    public ResponseEntity<Object> getBookingById(@Valid @Positive long userId, @Valid @Positive long bookingId) {
        return get(String.format("/%d", bookingId), userId);
    }

    @Override
    public ResponseEntity<Object> getUserBookings(
            @Valid @Positive long userId, String state, @Valid @PositiveOrZero int from, @Valid @Positive int size) {

        Matcher matcher = STATE_FILTER_PATTERN.matcher(state);
        if (!matcher.matches()) {
            throw new BadRequestParamException(String.format("Unknown state: %s", state));
        }
        Map<String, Object> parameters = Map.of(
                "state", state,
                "from", from,
                "size", size
        );
        return get("?from={from}&size={size}&state={state}", userId, parameters);
    }

    @Override
    public ResponseEntity<Object> getOwnerBookings(
            @Valid @Positive long ownerId, String state, @Valid @PositiveOrZero int from, @Valid @Positive int size) {

        Matcher matcher = STATE_FILTER_PATTERN.matcher(state);
        if (!matcher.matches()) {
            throw new BadRequestParamException(String.format("Unknown state: %s", state));
        }
        Map<String, Object> parameters = Map.of(
                "state", state,
                "from", from,
                "size", size
        );
        return get("/owner?from={from}&size={size}&state={state}", ownerId, parameters);
    }
}
