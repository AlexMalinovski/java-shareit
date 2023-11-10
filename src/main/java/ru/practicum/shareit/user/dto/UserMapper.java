package ru.practicum.shareit.user.dto;

import org.mapstruct.Mapper;
import ru.practicum.shareit.user.model.User;

@Mapper(componentModel = "spring")
public interface UserMapper {
    User mapCreateUserDtoToUser(CreateUserDto createUserDto);
    User mapUpdateUserDtoToUser(UpdateUserDto updateUserDto);
    UserDto mapUserToUserDto(User user);
}
