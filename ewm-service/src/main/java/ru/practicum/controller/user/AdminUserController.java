package ru.practicum.controller.user;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.model.dto.user.UserDto;
import ru.practicum.model.dto.request.NewUserRequest;
import ru.practicum.service.user.UserService;

import javax.validation.Valid;
import javax.validation.constraints.Min;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Validated
@RestController
@RequestMapping(path = "/admin/users")
public class AdminUserController {
	private final UserService userService;

	//GET /admin/users
	@GetMapping
	public List<UserDto> getUsers(@RequestParam(required = false) List<Integer> ids,
						   @RequestParam(defaultValue = "0") @Min(0) int from,
						   @RequestParam(defaultValue = "10") @Min(1) int size) {
		log.info("Request to get users");
		List<UserDto> users = userService.getUsers(ids, from, size);
		log.info("Users[{}] received.", users);
		return users;
	}

	//POST /admin/users
	@PostMapping
	@ResponseStatus(HttpStatus.CREATED)
	public UserDto addUser(@RequestBody @Valid NewUserRequest newUser) {
		log.info("Request to add new user[{}].", newUser);
		UserDto savedUser = userService.addUser(newUser);
		log.info("User[{}] saved.", savedUser);
		return savedUser;
	}

	//DELETE /admin/users/{userId}
	@DeleteMapping("/{userId}")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public UserDto deleteUser(@PathVariable @Min(1) int userId) {
		log.info("Request to delete user with userId[{}].", userId);
		UserDto deletedUser = userService.deleteUser(userId);
		log.info("User[{}] deleted.", deletedUser);
		return deletedUser;
	}
}