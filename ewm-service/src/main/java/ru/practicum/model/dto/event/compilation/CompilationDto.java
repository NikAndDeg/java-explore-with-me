package ru.practicum.model.dto.event.compilation;

import lombok.Builder;
import lombok.Data;
import ru.practicum.model.dto.event.EventShortDto;

import java.util.List;

@Data
@Builder
public class CompilationDto {
	private Integer id;
	private Boolean pinned;
	private String title;
	private List<EventShortDto> events;
}
