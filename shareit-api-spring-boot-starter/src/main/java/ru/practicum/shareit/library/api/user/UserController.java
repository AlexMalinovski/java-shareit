package ru.practicum.shareit.library.api.user;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import ru.practicum.shareit.library.api.user.dto.CreateUserDto;
import ru.practicum.shareit.library.api.user.dto.UpdateUserDto;

import javax.validation.Valid;
import javax.validation.constraints.Positive;

@RequestMapping(path = "/users")
public interface UserController {

    /**
     * Добавление нового пользователя.
     * Эндпойнт POST /users.
     *
     * @param createUserDto CreateUserDto
     * @return UserDto добавленного пользователя
     */
    @PostMapping
    ResponseEntity<Object> createUser(@RequestBody @Valid CreateUserDto createUserDto);

    /**
     * Просмотр информации о конкретном пользователе по его идентификатору.
     * Эндпойнт GET /users/{id}.
     *
     * @param id id пользователя
     * @return UserDto
     */
    @GetMapping("/{id}")
    ResponseEntity<Object> getUserById(@PathVariable @Valid @Positive long id);

    /**
     * Просмотр списка всех пользователей.
     * Эндпойнт GET /users.
     *
     * @return List<UserDto>
     */
    @GetMapping
    ResponseEntity<Object> getAllUsers();

    /**
     * Редактирование пользователя.
     * Эндпойнт PATCH /users/{id}.
     *
     * @param id id пользователя
     * @param updateUserDto UpdateUserDto
     * @return UserDto обновлённого пользователя
     */
    @PatchMapping("/{id}")
    ResponseEntity<Object> updateUser(@PathVariable @Valid @Positive long id,
                                       @RequestBody @Valid UpdateUserDto updateUserDto);

    /**
     * Удаление пользователя.
     * Эндпойнт DELETE /users/{id}.
     *
     * @param id id пользователя
     * @return UserDto удалённого пользователя
     */
    @DeleteMapping("/{id}")
    ResponseEntity<Object> deleteUser(@PathVariable @Valid @Positive long id);
}
