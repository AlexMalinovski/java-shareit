package ru.practicum.shareit.gateway.user.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.util.DefaultUriBuilderFactory;
import ru.practicum.shareit.gateway.client.BaseClient;
import ru.practicum.shareit.library.api.user.dto.CreateUserDto;
import ru.practicum.shareit.library.api.user.dto.UpdateUserDto;

import javax.validation.Valid;
import javax.validation.constraints.Positive;

@Service
@Validated
public class UserClientImpl extends BaseClient implements UserClient {
    private static final String API_PREFIX = "/users";

    @Autowired
    public UserClientImpl(@Value("${shareit-server.url}") String serverUrl, RestTemplateBuilder builder) {
        super(
                builder
                        .uriTemplateHandler(new DefaultUriBuilderFactory(serverUrl + API_PREFIX))
                        .requestFactory(HttpComponentsClientHttpRequestFactory::new)
                        .build()
        );
    }


    @Override
    public ResponseEntity<Object> createUser(@Valid CreateUserDto createUserDto) {
        return post("", createUserDto);
    }

    @Override
    public ResponseEntity<Object> getUserById(@Valid @Positive long id) {
        return get(String.format("/%d", id));
    }

    @Override
    public ResponseEntity<Object> getAllUsers() {
        return get("");
    }

    @Override
    public ResponseEntity<Object> updateUser(@Valid @Positive long id,
                                             @Valid UpdateUserDto updateUserDto) {
        return patch(String.format("/%d", id), updateUserDto);
    }

    @Override
    public ResponseEntity<Object> deleteUser(@Valid @Positive long id) {
        return delete(String.format("/%d", id));
    }
}
