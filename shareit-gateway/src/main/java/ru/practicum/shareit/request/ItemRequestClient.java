package ru.practicum.shareit.request;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.DefaultUriBuilderFactory;
import ru.practicum.shareit.client.BaseClient;
import ru.practicum.shareit.library.api.request.ItemRequestController;
import ru.practicum.shareit.library.api.request.dto.CreateItemRequestDto;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.util.Map;

@RestController
@Validated
public class ItemRequestClient extends BaseClient implements ItemRequestController {
    private static final String API_PREFIX = "/requests";

    @Autowired
    public ItemRequestClient(@Value("${shareit-server.url}") String serverUrl, RestTemplateBuilder builder) {
        super(
                builder
                        .uriTemplateHandler(new DefaultUriBuilderFactory(serverUrl + API_PREFIX))
                        .requestFactory(HttpComponentsClientHttpRequestFactory::new)
                        .build()
        );
    }

    @Override
    public ResponseEntity<Object> createItemRequest(
            @Valid @Positive long userId,
            @Valid CreateItemRequestDto dto) {
        return post("", userId, dto);
    }

    @Override
    public ResponseEntity<Object> getOwnedItemRequests(@Valid @Positive long ownerId) {
        return get("", ownerId);
    }

    @Override
    public ResponseEntity<Object> getItemRequests(
            @Valid @Positive long userId,
            @Valid @PositiveOrZero int from,
            @Valid @Positive int size) {
        Map<String, Object> parameters = Map.of(
                "from", from,
                "size", size
        );
        return get("/all", userId, parameters);
    }

    @Override
    public ResponseEntity<Object> getItemRequestById(
            @Valid @Positive long userId,
            @Valid @Positive long requestId) {
        return get(String.format("/%d", requestId), userId);
    }
}
