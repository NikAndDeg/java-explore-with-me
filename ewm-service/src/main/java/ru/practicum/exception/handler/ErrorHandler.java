package ru.practicum.exception.handler;

import jakarta.validation.ConstraintViolationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ru.practicum.exception.*;

import java.time.LocalDateTime;

@Slf4j
@RestControllerAdvice("ru.practicum")
public class ErrorHandler {
	@ExceptionHandler
	@ResponseStatus(HttpStatus.I_AM_A_TEAPOT)
	public ApiError handleTeaPotException(final TeaPotException e) {
		log.warn(e.getMessage());
		return new ApiError(e.getMessage(), "", HttpStatus.I_AM_A_TEAPOT.getReasonPhrase(), e.getTimestamp());
	}

	@ExceptionHandler
	@ResponseStatus(HttpStatus.CONFLICT)
	public ApiError handleDataConflict(final DataConflictException e) {
		log.warn(e.getMessage());
		return new ApiError(HttpStatus.CONFLICT.getReasonPhrase(),
				"Integrity constraint has been violated.",
				e.getMessage(),
				LocalDateTime.now());
	}

	@ExceptionHandler
	@ResponseStatus(HttpStatus.NOT_FOUND)
	public ApiError handleDataNotFound(final DataNotFoundException e) {
		log.warn(e.getMessage());
		return new ApiError(HttpStatus.NOT_FOUND.getReasonPhrase(),
				"The required object was not found.",
				e.getMessage(),
				LocalDateTime.now());
	}

	@ExceptionHandler
	@ResponseStatus(HttpStatus.FORBIDDEN)
	public ApiError handleDataNotConditional(final DataNotConditionalException e) {
		log.warn(e.getMessage());
		return new ApiError(HttpStatus.FORBIDDEN.getReasonPhrase(),
				"For the requested operation the conditions are not met.",
				e.getMessage(),
				LocalDateTime.now());
	}

	@ExceptionHandler
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	public ApiError handleBadRequestException(final BadRequestException e) {
		log.warn(e.getMessage());
		return new ApiError(HttpStatus.BAD_REQUEST.getReasonPhrase(),
				"For the requested operation the conditions are not met.",
				e.getMessage(),
				LocalDateTime.now());
	}

	@ExceptionHandler
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	public ApiError handleConstraintViolationException(final ConstraintViolationException e) {
		log.warn(e.getMessage());
		return new ApiError(HttpStatus.BAD_REQUEST.getReasonPhrase(),
				"For the requested operation the conditions are not met.",
				e.getMessage(),
				LocalDateTime.now());
	}
}
