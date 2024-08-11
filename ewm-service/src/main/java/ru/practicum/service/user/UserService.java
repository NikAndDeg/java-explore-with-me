package ru.practicum.service.user;

import ru.practicum.exception.DataConflictException;
import ru.practicum.exception.DataNotFoundException;
import ru.practicum.model.dto.request.NewUserRequest;
import ru.practicum.model.dto.user.UserDto;

import java.util.List;

public interface UserService {
	List<UserDto> getUsers(List<Integer> usersId, int from, int size);

	UserDto addUser(NewUserRequest newUser) throws DataConflictException;

	UserDto deleteUser(int userId) throws DataNotFoundException;
}
