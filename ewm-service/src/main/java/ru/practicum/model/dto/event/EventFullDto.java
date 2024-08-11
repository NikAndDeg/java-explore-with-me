package ru.practicum.model.dto.event;

import lombok.Builder;
import lombok.Data;
import ru.practicum.model.dto.event.category.CategoryDto;
import ru.practicum.model.dto.user.UserShortDto;
import ru.practicum.model.event.EventState;
import ru.practicum.model.event.location.Location;

import java.time.LocalDateTime;

@Data
@Builder
public class EventFullDto {
	private String annotation;
	private CategoryDto category;
	private int confirmedRequests;
	private LocalDateTime createdOn;
	private String description;
	private LocalDateTime eventDate;
	private Integer id;
	private UserShortDto initiator;
	private Location location;
	private boolean paid;
	private int participantLimit;
	private LocalDateTime publishedOn;
	private boolean requestModeration;
	private EventState state;
	private String title;
	private Long views;
}
