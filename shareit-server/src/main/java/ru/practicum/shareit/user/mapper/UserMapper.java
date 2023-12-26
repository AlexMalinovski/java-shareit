package ru.practicum.shareit.user.mapper;

import org.mapstruct.Mapper;
import ru.practicum.shareit.library.api.user.dto.CreateUserDto;
import ru.practicum.shareit.library.api.user.dto.UpdateUserDto;
import ru.practicum.shareit.library.api.user.dto.UserDto;
import ru.practicum.shareit.user.model.User;

import java.util.List;

@Mapper(componentModel = "spring")
public interface UserMapper {

    User mapCreateUserDtoToUser(CreateUserDto createUserDto);

    User mapUpdateUserDtoToUser(UpdateUserDto updateUserDto);

    UserDto mapUserToUserDto(User user);

    List<UserDto> mapUserToUserDto(List<User> user);
}
