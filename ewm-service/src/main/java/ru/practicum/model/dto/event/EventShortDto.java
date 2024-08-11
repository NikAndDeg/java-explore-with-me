package ru.practicum.model.dto.event;

import lombok.Builder;
import lombok.Data;
import ru.practicum.model.dto.event.category.CategoryDto;
import ru.practicum.model.dto.user.UserShortDto;

import java.time.LocalDateTime;

@Data
@Builder
public class EventShortDto {
	private Integer id;
	private String annotation;
	private CategoryDto category;
	private int confirmedRequests;
	private LocalDateTime eventDate;
	private UserShortDto initiator;
	private boolean paid;
	private String title;
	private Long views;
}
