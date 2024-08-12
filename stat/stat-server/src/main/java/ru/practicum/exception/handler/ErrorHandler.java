package ru.practicum.exception.handler;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ru.practicum.exception.BadRequestException;

import java.time.LocalDateTime;

@Slf4j
@RestControllerAdvice("ru.practicum")
public class ErrorHandler {
	@ExceptionHandler
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	public ApiError handleBadRequestException(final BadRequestException e) {
		log.warn(e.getMessage());
		return new ApiError(HttpStatus.BAD_REQUEST.getReasonPhrase(),
				"For the requested operation the conditions are not met.",
				e.getMessage(),
				LocalDateTime.now());
	}
}
