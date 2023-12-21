package ru.practicum.shareit.gateway.user.service;

import org.springframework.http.ResponseEntity;
import ru.practicum.shareit.library.api.user.dto.CreateUserDto;
import ru.practicum.shareit.library.api.user.dto.UpdateUserDto;

import javax.validation.Valid;
import javax.validation.constraints.Positive;

public interface UserClient {

    ResponseEntity<Object> createUser(@Valid CreateUserDto createUserDto);

    ResponseEntity<Object> getUserById(@Valid @Positive long id);

    ResponseEntity<Object> getAllUsers();

    ResponseEntity<Object> updateUser(@Valid @Positive long id, @Valid UpdateUserDto updateUserDto);

    ResponseEntity<Object> deleteUser(@Valid @Positive long id);
}
