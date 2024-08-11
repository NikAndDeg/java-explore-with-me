package ru.practicum.model.dto.request;

import lombok.Data;
import ru.practicum.model.event.participation.ParticipationStatus;

import java.util.List;

@Data
public class EventRequestStatusUpdateRequest {
	private List<Integer> requestIds;
	private ParticipationStatus status;
}
