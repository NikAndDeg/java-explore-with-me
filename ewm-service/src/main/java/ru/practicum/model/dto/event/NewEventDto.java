package ru.practicum.model.dto.event;

import lombok.Data;
import ru.practicum.model.event.location.Location;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.time.LocalDateTime;

@Data
public class NewEventDto {
	@NotBlank
	@NotNull
	@Size(min = 20, max = 2000)
	private String annotation;
	private int category;
	@NotBlank
	@NotNull
	@Size(min = 20, max = 7000)
	private String description;
	@NotNull
	private LocalDateTime eventDate;
	@NotNull
	private Location location;
	private Boolean paid = false;
	@Min(0)
	private int participantLimit = 0;
	private Boolean requestModeration = true;
	@NotBlank
	@NotNull
	@Size(min = 3, max = 120)
	private String title;
}
