package ru.practicum.shareit.user;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.library.api.user.UserController;
import ru.practicum.shareit.library.api.user.dto.CreateUserDto;
import ru.practicum.shareit.library.api.user.dto.UpdateUserDto;
import ru.practicum.shareit.library.api.user.dto.UserDto;
import ru.practicum.shareit.user.mapper.UserMapper;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.service.UserService;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import java.util.List;

@RestController
@Validated
@RequiredArgsConstructor
public class UserControllerImpl implements UserController {
    private final UserService userService;
    private final UserMapper userMapper;

    @Override
    public ResponseEntity<Object> createUser(@RequestBody @Valid CreateUserDto createUserDto) {
        final User user = userMapper.mapCreateUserDtoToUser(createUserDto);
        User createdUser = userService.createUser(user);
        return ResponseEntity.ok(userMapper.mapUserToUserDto(createdUser));
    }

    @Override
    public ResponseEntity<Object> getUserById(@PathVariable @Valid @Positive long id) {
        User user = userService.getUserById(id)
                .orElseThrow(() -> new NotFoundException("Не найден пользователь с id=" + id));
        return ResponseEntity.ok(userMapper.mapUserToUserDto(user));
    }

    @Override
    public ResponseEntity<List<?>> getAllUsers() {
        List<UserDto> users = userMapper.mapUserToUserDto(userService.getAllUsers());
        return ResponseEntity.ok(users);
    }

    @Override
    public ResponseEntity<Object> updateUser(@PathVariable @Valid @Positive long id,
                                              @RequestBody @Valid UpdateUserDto updateUserDto) {
        final User user = userMapper.mapUpdateUserDtoToUser(updateUserDto)
                .toBuilder()
                .id(id)
                .build();
        User updatedUser = userService.updateUser(user);
        return ResponseEntity.ok(userMapper.mapUserToUserDto(updatedUser));
    }

    @Override
    public ResponseEntity<Object> deleteUser(@PathVariable @Valid @Positive long id) {
        User deletedUser = userService.deleteUser(id)
                .orElseThrow(() -> new NotFoundException("Не найден пользователь с id=" + id));
        return ResponseEntity.ok(userMapper.mapUserToUserDto(deletedUser));
    }
}
