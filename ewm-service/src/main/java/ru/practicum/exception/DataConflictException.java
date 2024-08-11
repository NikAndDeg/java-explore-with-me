package ru.practicum.exception;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DataConflictException extends RuntimeException {
	public DataConflictException(String message) {
		super(message);
	}
}
