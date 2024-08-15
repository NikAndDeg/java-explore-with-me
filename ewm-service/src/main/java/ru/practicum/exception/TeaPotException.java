package ru.practicum.exception;

import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class TeaPotException extends RuntimeException {
	private final LocalDateTime timestamp;

	public TeaPotException(String message, LocalDateTime timestamp) {
		super(message);
		this.timestamp = timestamp;
	}
}
