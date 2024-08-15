package ru.practicum.model.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.data.domain.Pageable;
import ru.practicum.model.event.EventSort;

import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
public class PublicGetEventsRequest {
	private String text;
	private List<Integer> categories;
	private Boolean paid;
	private LocalDateTime start;
	private LocalDateTime end;
	private Boolean onlyAvailable;
	private EventSort sort;
	private Pageable pageable;
}
