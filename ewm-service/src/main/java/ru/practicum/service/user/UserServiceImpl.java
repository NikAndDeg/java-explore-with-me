package ru.practicum.service.user;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import ru.practicum.exception.DataConflictException;
import ru.practicum.exception.DataNotFoundException;
import ru.practicum.util.mapper.UserMapper;
import ru.practicum.model.dto.user.UserDto;
import ru.practicum.model.dto.request.NewUserRequest;
import ru.practicum.model.entity.UserEntity;
import ru.practicum.repostitory.UserRepository;
import ru.practicum.util.Pagenator;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
	private final UserRepository userRepository;

	@Override
	public List<UserDto> getUsers(List<Integer> usersId, int from, int size) {
		List<UserEntity> users;
		if (usersId != null)
			users = userRepository.findAllByIdIn(usersId, Pagenator.getPageable(from, size));
		else
			users = userRepository.findAll(Pagenator.getPageable(from, size)).getContent();
		return users.stream()
				.map(UserMapper::entityToDto)
				.collect(Collectors.toList());
	}

	@Override
	public UserDto addUser(NewUserRequest newUser) throws DataConflictException {
		try {
			UserEntity savedUserEntity = userRepository.save(UserMapper.newUserRequestRoEntity(newUser));
			return UserMapper.entityToDto(savedUserEntity);
		} catch (DataIntegrityViolationException e) {
			throw new DataConflictException(e.getMessage());
		}
	}

	@Override
	public UserDto deleteUser(int userId) throws DataNotFoundException {
		UserEntity userToDeleteEntity = userRepository.findById(userId).orElseThrow(
				() -> new DataNotFoundException("User with id [" + userId + "] doesn't exist.")
		);
		userRepository.deleteById(userId);
		return UserMapper.entityToDto(userToDeleteEntity);
	}
}