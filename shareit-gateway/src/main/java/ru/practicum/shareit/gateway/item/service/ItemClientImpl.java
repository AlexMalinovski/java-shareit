package ru.practicum.shareit.gateway.item.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.util.DefaultUriBuilderFactory;
import ru.practicum.shareit.gateway.client.BaseClient;
import ru.practicum.shareit.library.api.item.dto.CreateCommentDto;
import ru.practicum.shareit.library.api.item.dto.CreateItemDto;
import ru.practicum.shareit.library.api.item.dto.UpdateItemDto;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.util.Map;

@Service
@Validated
public class ItemClientImpl extends BaseClient implements ItemClient {
    private static final String API_PREFIX = "/items";

    @Autowired
    public ItemClientImpl(@Value("${shareit-server.url}") String serverUrl, RestTemplateBuilder builder) {
        super(
                builder
                        .uriTemplateHandler(new DefaultUriBuilderFactory(serverUrl + API_PREFIX))
                        .requestFactory(HttpComponentsClientHttpRequestFactory::new)
                        .build()
        );
    }

    @Override
    public ResponseEntity<Object> createItem(@Valid @Positive long userId,
                                             @Valid CreateItemDto createItemDto) {
        return post("", userId, createItemDto);
    }

    @Override
    public ResponseEntity<Object> getItemById(@Valid @Positive long userId,
                                              @Valid @Positive long itemId) {
        return get(String.format("/%d", itemId), userId);
    }

    @Override
    public ResponseEntity<Object> updateItem(@Valid @Positive long userId,
                                             @Valid @Positive long itemId,
                                             UpdateItemDto updateItemDto) {
        return patch(String.format("/%d", itemId), userId, updateItemDto);
    }

    @Override
    public ResponseEntity<Object> getOwnedItems(@Valid @Positive long userId,
                                                 @Valid @PositiveOrZero int from,
                                                 @Valid @Positive int size) {
        Map<String, Object> parameters = Map.of(
                "from", from,
                "size", size
        );
        return get("?from={from}&size={size}", userId, parameters);
    }

    @Override
    public ResponseEntity<Object> searchItems(@Valid @Positive long userId,
                                               String text,
                                               @Valid @PositiveOrZero int from,
                                               @Valid @Positive int size) {
        Map<String, Object> parameters = Map.of(
                "from", from,
                "size", size,
                "text", text
        );
        return get("/search?from={from}&size={size}&text={text}", userId, parameters);
    }

    @Override
    public ResponseEntity<Object> createComment(@Valid @Positive long userId,
                                                @Valid @Positive long itemId,
                                                @Valid CreateCommentDto createCommentDto) {
        return post(String.format("/%d/comment", itemId), userId, createCommentDto);
    }
}
