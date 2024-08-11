package ru.practicum.model.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import ru.practicum.model.event.EventState;

import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
public class GetEventsServiceRequest {
	private List<Integer> usersId;
	private List<EventState> states;
	private List<Integer> categoriesId;
	private LocalDateTime start;
	private LocalDateTime end;
	private int from;
	private int size;
}
