package ru.practicum.util.mapper;

import lombok.experimental.UtilityClass;
import ru.practicum.model.dto.request.NewUserRequest;
import ru.practicum.model.dto.user.UserDto;
import ru.practicum.model.dto.user.UserShortDto;
import ru.practicum.model.entity.UserEntity;

@UtilityClass
public class UserMapper {
	public static UserDto entityToDto(UserEntity entity) {
		UserDto dto = new UserDto();
		dto.setId(entity.getId());
		dto.setName(entity.getName());
		dto.setEmail(entity.getEmail());
		return dto;
	}

	public static UserShortDto entityToShortDto(UserEntity entity) {
		UserShortDto shortDto = new UserShortDto();
		shortDto.setId(entity.getId());
		shortDto.setName(entity.getName());
		return shortDto;
	}

	public static UserEntity newUserRequestRoEntity(NewUserRequest newUser) {
		UserEntity entity = new UserEntity();
		entity.setName(newUser.getName());
		entity.setEmail(newUser.getEmail());
		return entity;
	}
}
