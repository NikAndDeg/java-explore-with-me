package ru.practicum.model.dto.request;

import lombok.Data;
import lombok.NoArgsConstructor;
import ru.practicum.model.dto.event.participation.ParticipationRequestDto;

import java.util.List;

@Data
@NoArgsConstructor
public class EventRequestStatusUpdateResult {
	private List<ParticipationRequestDto> confirmedRequests;
	private List<ParticipationRequestDto> rejectedRequests;
}
