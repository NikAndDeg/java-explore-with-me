package ru.practicum.util.mapper;

import lombok.experimental.UtilityClass;
import ru.practicum.model.dto.event.participation.ParticipationRequestDto;
import ru.practicum.model.dto.request.EventRequestStatusUpdateResult;
import ru.practicum.model.entity.ParticipationRequestEntity;
import ru.practicum.model.event.participation.ParticipationStatus;

import java.util.ArrayList;
import java.util.List;

@UtilityClass
public class ParticipationRequestMapper {
	public static ParticipationRequestDto entityToDto(ParticipationRequestEntity entity, int eventId) {
		return ParticipationRequestDto.builder()
				.id(entity.getId())
				.created(entity.getCreatedOn())
				.event(eventId)
				.requester(entity.getRequester().getId())
				.status(entity.getStatus())
				.build();
	}

	public static ParticipationRequestDto entityToDto(ParticipationRequestEntity entity) {
		return ParticipationRequestDto.builder()
				.id(entity.getId())
				.created(entity.getCreatedOn())
				.event(entity.getEvent().getId())
				.requester(entity.getRequester().getId())
				.status(entity.getStatus())
				.build();
	}

	public static EventRequestStatusUpdateResult entitiesStatusUpdateResult(List<ParticipationRequestEntity> requests,
																			int eventId) {
		EventRequestStatusUpdateResult result = new EventRequestStatusUpdateResult();
		List<ParticipationRequestDto> confirmedRequests = new ArrayList<>();
		List<ParticipationRequestDto> rejectedRequests = new ArrayList<>();

		requests.forEach(request -> {
			if (request.getStatus().equals(ParticipationStatus.CONFIRMED))
				confirmedRequests.add(entityToDto(request, eventId));
			else if (request.getStatus().equals(ParticipationStatus.REJECTED))
				rejectedRequests.add(entityToDto(request, eventId));
		});

		result.setConfirmedRequests(confirmedRequests);
		result.setRejectedRequests(rejectedRequests);
		return result;
	}
}
