package ru.practicum.exception.handler;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class ApiError {
	private String status;
	private String reason;
	private String message;
	private LocalDateTime timestamp;
	//errors ???
}
