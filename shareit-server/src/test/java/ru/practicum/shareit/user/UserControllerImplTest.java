package ru.practicum.shareit.user;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.shareit.library.api.exception.ConflictException;
import ru.practicum.shareit.library.api.user.dto.CreateUserDto;
import ru.practicum.shareit.library.api.user.dto.UpdateUserDto;
import ru.practicum.shareit.library.api.user.dto.UserDto;
import ru.practicum.shareit.user.mapper.UserMapper;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.service.UserService;

import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(UserControllerImpl.class)
class UserControllerImplTest {

    @MockBean
    private UserService userService;

    @MockBean
    private UserMapper userMapper;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    private CreateUserDto getValidCreateUserDto() {
        return CreateUserDto.builder()
                .name("name")
                .email("name@e.mail")
                .build();
    }

    private User getValidNewUser() {
        return User.builder()
                .name("name")
                .email("name@e.mail")
                .build();
    }

    private User getValidUser() {
        return User.builder()
                .id(1L)
                .name("name")
                .email("name@e.mail")
                .build();
    }

    private UserDto getValidUserDto() {
        return UserDto.builder()
                .id(1L)
                .name("name")
                .email("name@e.mail")
                .build();
    }

    private UpdateUserDto getValidUpdateUserDto() {
        return UpdateUserDto.builder()
                .name("name")
                .email("name@e.mail")
                .build();
    }

    @Test
    void createUser_isAvailable() throws Exception {
        when(userMapper.mapCreateUserDtoToUser(any())).thenReturn(getValidNewUser());
        when(userMapper.mapUserToUserDto(any(User.class))).thenReturn(getValidUserDto());
        when(userService.createUser(any())).thenReturn(getValidUser());

        mockMvc.perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(getValidCreateUserDto())))
                .andExpect(status().isOk());
    }

    @Test
    void createUser_ifConflictEmail_thenStatus409() throws Exception {
        when(userMapper.mapCreateUserDtoToUser(any())).thenReturn(getValidNewUser());
        when(userMapper.mapUserToUserDto(any(User.class))).thenReturn(getValidUserDto());
        when(userService.createUser(any())).thenThrow(ConflictException.class);

        mockMvc.perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(getValidCreateUserDto())))
                .andExpect(status().isConflict());
    }

    @Test
    void getUserById_isAvailable() throws Exception {
        when(userMapper.mapUserToUserDto(any(User.class))).thenReturn(getValidUserDto());
        when(userService.getUserById(1L)).thenReturn(Optional.of(getValidUser()));

        mockMvc.perform(get("/users/1"))
                .andExpect(status().isOk());
    }

    @Test
    void getAllUsers_isAvailable() throws Exception {
        when(userMapper.mapUserToUserDto(any(User.class))).thenReturn(getValidUserDto());
        when(userService.getAllUsers()).thenReturn(List.of(getValidUser()));

        mockMvc.perform(get("/users"))
                .andExpect(status().isOk());
    }

    @Test
    void updateUser_isAvailable() throws Exception {
        when(userMapper.mapUpdateUserDtoToUser(any())).thenReturn(getValidNewUser());
        when(userMapper.mapUserToUserDto(any(User.class))).thenReturn(getValidUserDto());
        when(userService.updateUser(any())).thenReturn(getValidUser());

        mockMvc.perform(patch("/users/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(getValidUpdateUserDto())))
                .andExpect(status().isOk());
    }

    @Test
    void deleteUser_isAvailable() throws Exception {
        when(userMapper.mapUserToUserDto(any(User.class))).thenReturn(getValidUserDto());
        when(userService.deleteUser(1L)).thenReturn(Optional.of(getValidUser()));

        mockMvc.perform(delete("/users/1"))
                .andExpect(status().isOk());
    }
}