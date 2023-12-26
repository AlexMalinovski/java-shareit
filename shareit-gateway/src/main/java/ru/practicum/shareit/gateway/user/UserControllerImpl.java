package ru.practicum.shareit.gateway.user;


import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import ru.practicum.shareit.gateway.user.service.UserClient;
import ru.practicum.shareit.library.api.user.UserController;
import ru.practicum.shareit.library.api.user.dto.CreateUserDto;
import ru.practicum.shareit.library.api.user.dto.UpdateUserDto;

@RestController
@RequiredArgsConstructor
public class UserControllerImpl implements UserController {
    private final UserClient userClient;

    @Override
    public ResponseEntity<Object> createUser(CreateUserDto createUserDto) {
        return userClient.createUser(createUserDto);
    }

    @Override
    public ResponseEntity<Object> getUserById(long id) {
        return userClient.getUserById(id);
    }

    @Override
    public ResponseEntity<Object> getAllUsers() {
        return userClient.getAllUsers();
    }

    @Override
    public ResponseEntity<Object> updateUser(long id, UpdateUserDto updateUserDto) {
        return userClient.updateUser(id, updateUserDto);
    }

    @Override
    public ResponseEntity<Object> deleteUser(long id) {
        return userClient.deleteUser(id);
    }
}
