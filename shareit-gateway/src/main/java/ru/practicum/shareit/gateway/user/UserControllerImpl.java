package ru.practicum.shareit.gateway.user;


import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import ru.practicum.shareit.gateway.user.service.UserClient;
import ru.practicum.shareit.library.api.user.UserController;
import ru.practicum.shareit.library.api.user.dto.CreateUserDto;
import ru.practicum.shareit.library.api.user.dto.UpdateUserDto;

@Tag(name = "Пользователи", description = "API для работы с пользователями")
@RestController
@RequiredArgsConstructor
public class UserControllerImpl implements UserController {
    private final UserClient userClient;

    @Override
    @Operation(summary = "Добавление нового пользователя")
    public ResponseEntity<Object> createUser(CreateUserDto createUserDto) {
        return userClient.createUser(createUserDto);
    }

    @Override
    @Operation(summary = "Получение данных о пользователе по id")
    public ResponseEntity<Object> getUserById(long id) {
        return userClient.getUserById(id);
    }

    @Override
    @Operation(summary = "Получение списка всех пользователей")
    public ResponseEntity<Object> getAllUsers() {
        return userClient.getAllUsers();
    }

    @Override
    @Operation(summary = "Редактирование пользователя")
    public ResponseEntity<Object> updateUser(long id, UpdateUserDto updateUserDto) {
        return userClient.updateUser(id, updateUserDto);
    }

    @Override
    @Operation(summary = "Удаление пользователя")
    public ResponseEntity<Object> deleteUser(long id) {
        return userClient.deleteUser(id);
    }
}
