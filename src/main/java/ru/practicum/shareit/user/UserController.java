package ru.practicum.shareit.user;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.user.dto.CreateUserDto;
import ru.practicum.shareit.user.dto.UpdateUserDto;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.dto.UserMapper;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.service.UserService;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping(path = "/users")
@Validated
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;
    private final UserMapper userMapper;

    /**
     * Добавление нового пользователя.
     * Эндпойнт POST /users.
     * @param createUserDto CreateUserDto
     * @return UserDto добавленного пользователя
     */
    @PostMapping
    public ResponseEntity<UserDto> createUser(@RequestBody @Valid CreateUserDto createUserDto) {
        final User user = userMapper.mapCreateUserDtoToUser(createUserDto);
        User createdUser = userService.createUser(user);
        return ResponseEntity.ok(userMapper.mapUserToUserDto(createdUser));
    }

    /**
     * Просмотр информации о конкретном пользователе по его идентификатору.
     * Эндпойнт GET /users/{id}.
     * @param id id пользователя
     * @return UserDto
     */
    @GetMapping("/{id}")
    public ResponseEntity<UserDto> getUserById(@PathVariable @Valid @Positive long id) {
        User user = userService.getUserById(id)
                .orElseThrow(() -> new NotFoundException("Не найден пользователь с id=" + id));
        return ResponseEntity.ok(userMapper.mapUserToUserDto(user));
    }

    /**
     * Просмотр списка всех пользователей.
     * Эндпойнт GET /users.
     * @return List<UserDto>
     */
    @GetMapping
    public ResponseEntity<List<UserDto>> getAllUsers() {
        List<UserDto> users = userService.getAllUsers()
                .stream()
                .map(userMapper::mapUserToUserDto)
                .collect(Collectors.toList());
        return ResponseEntity.ok(users);
    }

    /**
     * Редактирование пользователя.
     * Эндпойнт PATCH /users/{id}.
     * @param id id пользователя
     * @param updateUserDto UpdateUserDto
     * @return UserDto обновлённого пользователя
     */
    @PatchMapping("/{id}")
    public ResponseEntity<UserDto> updateUser(@PathVariable @Valid @Positive long id,
                                              @RequestBody @Valid UpdateUserDto updateUserDto) {
        final User user = userMapper.mapUpdateUserDtoToUser(updateUserDto);
        user.setId(id);
        User updatedUser = userService.updateUser(user);
        return ResponseEntity.ok(userMapper.mapUserToUserDto(updatedUser));
    }

    /**
     * Удаление пользователя.
     * Эндпойнт DELETE /users/{id}.
     * @param id id пользователя
     * @return UserDto удалённого пользователя
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<UserDto> deleteUser(@PathVariable @Valid @Positive long id) {
        User deletedUser = userService.deleteUser(id)
                .orElseThrow(() -> new NotFoundException("Не найден пользователь с id=" + id));
        return ResponseEntity.ok(userMapper.mapUserToUserDto(deletedUser));
    }
}
