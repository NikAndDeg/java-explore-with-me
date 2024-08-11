package ru.practicum.model.dto.request;

import lombok.Data;
import ru.practicum.model.event.EventStateActionUser;
import ru.practicum.model.event.location.Location;

import javax.validation.constraints.Min;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;

@Data
public class UpdateEventUserRequest {
	@Size(min = 20, max = 2000)
	private String annotation;
	@Min(1)
	private Integer category;
	@Size(min = 20, max = 7000)
	private String description;
	private LocalDateTime eventDate;
	private Location location;
	private Boolean paid;
	@Min(0)
	private Integer participantLimit;
	private Boolean requestModeration;
	private EventStateActionUser stateAction;
	@Size(min = 3, max = 120)
	private String title;
}
