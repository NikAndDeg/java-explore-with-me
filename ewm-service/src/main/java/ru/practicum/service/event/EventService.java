package ru.practicum.service.event;

import ru.practicum.exception.BadRequestException;
import ru.practicum.exception.DataConflictException;
import ru.practicum.exception.DataNotConditionalException;
import ru.practicum.exception.DataNotFoundException;
import ru.practicum.model.dto.event.EventFullDto;
import ru.practicum.model.dto.event.EventShortDto;
import ru.practicum.model.dto.event.NewEventDto;
import ru.practicum.model.dto.event.participation.ParticipationRequestDto;
import ru.practicum.model.dto.request.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

public interface EventService {
	List<EventFullDto> getEvents(GetEventsServiceRequest getEventsRequest) throws BadRequestException;

	EventFullDto updateEvent(int eventId, UpdateEventAdminRequest request) throws BadRequestException,
			DataNotFoundException, DataNotConditionalException;

	List<EventShortDto> getUserEvents(int userId, int from, int size) throws DataNotFoundException;

	EventFullDto addEvent(int userId, NewEventDto newEventDto) throws BadRequestException, DataNotFoundException;

	EventFullDto getUserEvent(int userId, int eventId) throws DataNotFoundException;

	EventFullDto updateEvent(int userId, int eventId, UpdateEventUserRequest updateRequest) throws DataNotFoundException,
			BadRequestException, DataConflictException;

	List<ParticipationRequestDto> getParticipationRequests(int userId, int eventId);

	EventRequestStatusUpdateResult updateEventRequestStatus(int userId, int eventId, EventRequestStatusUpdateRequest updateRequest)
			throws DataNotFoundException, DataNotConditionalException, DataConflictException;

	List<ParticipationRequestDto> getUserParticipationRequests(int userId) throws DataNotFoundException;

	ParticipationRequestDto addParticipationRequest(int userId, int eventId) throws DataNotFoundException,
			DataConflictException;

	ParticipationRequestDto cancelParticipationRequest(int userId, int requestId) throws DataNotFoundException;

	List<EventShortDto> getPublicEvents(PublicGetEventsRequest request, HttpServletRequest httpServletRequest) throws BadRequestException;

	EventFullDto getEvent(int eventId, HttpServletRequest httpRequest) throws DataNotFoundException;
}
