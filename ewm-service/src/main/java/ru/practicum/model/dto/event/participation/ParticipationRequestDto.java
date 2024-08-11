package ru.practicum.model.dto.event.participation;

import lombok.Builder;
import lombok.Data;
import ru.practicum.model.event.participation.ParticipationStatus;

import java.time.LocalDateTime;

@Data
@Builder
public class ParticipationRequestDto {
	private Integer id;
	private LocalDateTime created;
	private int event;
	private int requester;
	private ParticipationStatus status;
}
