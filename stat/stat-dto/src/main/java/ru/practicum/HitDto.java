package ru.practicum;

import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;

@Data
@Builder
public class HitDto {
	private static final int maxSize = 50;
	public static final String maxSizeMessage = "size must be between 0 and " + maxSize;

	private Integer id;

	@NotNull
	@Size(max = maxSize, message = maxSizeMessage)
	private String app;

	@NotNull
	@Size(max = maxSize, message = maxSizeMessage)
	private String uri;

	@NotNull
	@Size(max = maxSize, message = maxSizeMessage)
	private String ip;

	@NotNull
	private LocalDateTime timestamp;
}
