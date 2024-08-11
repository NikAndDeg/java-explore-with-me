package ru.practicum.service.event;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import ru.practicum.StatClient;
import ru.practicum.StatDto;
import ru.practicum.exception.BadRequestException;
import ru.practicum.exception.DataNotConditionalException;
import ru.practicum.exception.DataConflictException;
import ru.practicum.exception.DataNotFoundException;
import ru.practicum.model.dto.event.EventFullDto;
import ru.practicum.model.dto.event.EventShortDto;
import ru.practicum.model.dto.event.NewEventDto;
import ru.practicum.model.dto.event.participation.ParticipationRequestDto;
import ru.practicum.model.dto.request.*;
import ru.practicum.model.entity.*;
import ru.practicum.model.event.EventSort;
import ru.practicum.model.event.EventState;
import ru.practicum.model.event.EventStateActionAdmin;
import ru.practicum.model.event.EventStateActionUser;
import ru.practicum.model.event.participation.ParticipationStatus;
import ru.practicum.repostitory.CategoryRepository;
import ru.practicum.repostitory.EventRepository;
import ru.practicum.repostitory.RequestRepository;
import ru.practicum.repostitory.UserRepository;
import ru.practicum.util.Pagenator;
import ru.practicum.util.mapper.EventMapper;
import ru.practicum.util.mapper.LocationMapper;
import ru.practicum.util.mapper.ParticipationRequestMapper;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class EventServiceImpl implements EventService {
	@Value("${app.name}")
	private String appName;
	private final StatClient statClient;
	private final WebClient webClient;

	private final EventRepository eventRepository;
	private final UserRepository userRepository;
	private final CategoryRepository categoryRepository;
	private final RequestRepository requestRepository;

	@Override
	public List<EventFullDto> getEvents(GetEventsServiceRequest request) throws BadRequestException {
		if (request.getStart() != null && request.getEnd() != null)
			if (request.getStart().isAfter(request.getEnd()))
				throw new BadRequestException("Range start cannot be after range end.");

		List<EventEntity> events = eventRepository.findWithCategoryInitiatorLocationBySearchParam(
				request.getUsersId(),
				request.getStates(),
				request.getCategoriesId(),
				request.getStart(),
				request.getEnd(),
				Pagenator.getPageable(request.getFrom(), request.getSize()));

		List<EventFullDto> eventFullDtos = events.stream()
				.map(EventMapper::entityToFullDto)
				.collect(Collectors.toList());

		setViewsToEventFullDtos(eventFullDtos);

		return eventFullDtos;
	}

	@Override
	public EventFullDto updateEvent(int eventId, UpdateEventAdminRequest request) throws BadRequestException,
			DataNotFoundException, DataNotConditionalException {

		if (request.getEventDate() != null && request.getEventDate().isBefore(LocalDateTime.now().plusHours(2)))
			throw new BadRequestException("Field: eventDate. " +
					"Error: должно содержать дату, которая еще не наступила. " +
					"Value: " + request.getEventDate());

		EventEntity eventToUpdate = eventRepository.findWithCategoryInitiatorLocationById(eventId).orElseThrow(
				() -> new DataNotFoundException("Event with id[" + eventId + "] doesn't exist.")
		);

		if (eventToUpdate.getEventDate().isBefore(LocalDateTime.now().plusHours(1)))
			throw new DataConflictException("Event date mustn't be earlier than one hour from the publication date.");

		if (!eventToUpdate.getState().equals(EventState.PENDING))
			throw new DataConflictException("Event state must be PENDING.");

		setNewInfoForEventToUpdateByAdmin(eventToUpdate, request);

		return EventMapper.entityToFullDto(eventRepository.save(eventToUpdate));
	}

	@Override
	public List<EventShortDto> getUserEvents(int userId, int from, int size) throws DataNotFoundException {
		UserEntity user = getUserEntityOrThrowException(userId);

		List<EventEntity> userEvents = eventRepository.findWithCategoryAllByInitiatorId(userId,
				Pagenator.getPageable(from, size));

		return userEvents.stream()
				.map(event -> EventMapper.entityToShortDto(event, user))
				.collect(Collectors.toList());
	}

	@Override
	public EventFullDto addEvent(int userId, NewEventDto newEventDto) throws BadRequestException,
			DataNotFoundException {

		if (newEventDto.getEventDate().isBefore(LocalDateTime.now().plusHours(2)))
			throw new BadRequestException("Field: eventDate. " +
					"Error: должно содержать дату, которая еще не наступила. " +
					"Value: " + newEventDto.getEventDate());

		UserEntity user = getUserEntityOrThrowException(userId);

		CategoryEntity category = getCategoryEntityOrThrowException(newEventDto.getCategory());

		LocationEntity location = LocationMapper.dtoToEntity(newEventDto.getLocation());

		EventEntity eventToSave = EventMapper.newEventDtoToEntity(newEventDto);

		eventToSave.setInitiator(user);

		eventToSave.setCategory(category);

		eventToSave.setLocation(location);



		return EventMapper.entityToFullDto(eventRepository.save(eventToSave));
	}

	@Override
	public EventFullDto getUserEvent(int userId, int eventId) throws DataNotFoundException {
		EventEntity eventEntity = eventRepository.findWithCategoryInitiatorLocationById(eventId).orElseThrow(
				() -> new DataNotFoundException("Event with id[" + + eventId + "] not found.")
		);

		if (!eventEntity.getInitiator().getId().equals(userId))
			throw new DataNotFoundException("Event with id[" + + eventId + "] not found.");

		EventFullDto eventDto = EventMapper.entityToFullDto(eventEntity);

		eventDto.setViews(getEventViews(eventId));

		return eventDto;
	}

	@Override
	public EventFullDto updateEvent(int userId, int eventId, UpdateEventUserRequest updateRequest) throws DataNotFoundException,
			BadRequestException, DataConflictException {

		if (updateRequest.getEventDate() != null && updateRequest.getEventDate().isBefore(LocalDateTime.now().plusHours(2)))
			throw new BadRequestException("Field: eventDate. " +
					"Error: должно содержать дату, которая еще не наступила. " +
					"Value: " + updateRequest.getEventDate());

		EventEntity eventToUpdate = eventRepository.findWithCategoryInitiatorLocationById(eventId).orElseThrow(
				() -> new DataNotFoundException("Event with id[" + eventId + "] doesn't exist.")
		);

		if (!eventToUpdate.getInitiator().getId().equals(userId))
			throw new DataNotFoundException("Event with id[" + + eventId + "] not found.");

		if (eventToUpdate.getState().equals(EventState.PUBLISHED))
			throw new DataConflictException("Event with id[" + eventId + "] is published.");

		setNewInfoForEventToUpdateByUser(eventToUpdate, updateRequest);

		EventFullDto updatedEvent = EventMapper.entityToFullDto(eventRepository.save(eventToUpdate));

		updatedEvent.setViews(getEventViews(eventId));

		return updatedEvent;
	}

	@Override
	public List<ParticipationRequestDto> getParticipationRequests(int userId, int eventId) {
		EventEntity event = eventRepository.findWithInitiatorRequestsWithRequesterById(eventId).orElseThrow(
				() -> new DataNotFoundException("Event with id[" + eventId + "] doesn't exist.")
		);

		if (!event.getInitiator().getId().equals(userId))
			throw new DataNotFoundException("Event with id[" + + eventId + "] not found.");

		List<ParticipationRequestEntity> requests = event.getRequests();

		if (requests.isEmpty())
			return List.of();

		return requests.stream()
				.map(entity -> ParticipationRequestMapper.entityToDto(entity, event.getId()))
				.collect(Collectors.toList());
	}

	@Override
	public EventRequestStatusUpdateResult updateEventRequestStatus(int userId, int eventId,
																   EventRequestStatusUpdateRequest updateRequest)
			throws DataNotFoundException, DataNotConditionalException, DataConflictException {

		EventEntity event = eventRepository.findWithInitiatorRequestsWithRequesterById(eventId).orElseThrow(
				() -> new DataNotFoundException("Event with id[" + eventId + "] doesn't exist.")
		);

		if (!event.getRequestModeration() || event.getParticipantLimit().equals(0))
			throw new DataNotConditionalException("Event doesn't require confirmation or rejection of requests.");

		if (!event.getParticipantLimit().equals(0) && event.getParticipantLimit().equals(event.getConfirmedRequests())
				&& updateRequest.getStatus().equals(ParticipationStatus.CONFIRMED))
			throw new DataConflictException("The participant limit has been reached");

		setNewStatusToParticipationRequests(event, updateRequest);

		eventRepository.save(event);

		return ParticipationRequestMapper.entitiesStatusUpdateResult(event.getRequests(), event.getId());
	}

	@Override
	public List<ParticipationRequestDto> getUserParticipationRequests(int userId) throws DataNotFoundException {
		UserEntity user = userRepository.findWithRequestsAndEventsById(userId).orElseThrow(
				() -> new DataNotFoundException("User with id [" + userId + "] doesn't exist.")
		);
		List<ParticipationRequestEntity> requests = user.getRequests();
		if (requests.isEmpty())
			return List.of();
		return requests.stream()
				.map(ParticipationRequestMapper::entityToDto)
				.collect(Collectors.toList());
	}

	@Override
	public ParticipationRequestDto addParticipationRequest(int userId, int eventId) throws DataNotFoundException,
			DataConflictException {

		UserEntity user = userRepository.findWithRequestsById(userId).orElseThrow(
				() -> new DataNotFoundException("User with id [" + userId + "] doesn't exist.")
		);

		List<Integer> userRequestsEventsId = user.getRequests().stream()
				.map(request -> request.getEvent().getId())
				.collect(Collectors.toList());
		if (userRequestsEventsId.contains(eventId))
			throw new DataConflictException("Cannot add duplicate request.");


		EventEntity event = eventRepository.findById(eventId).orElseThrow(
				() -> new DataNotFoundException("Event with id[" + eventId + "] doesn't exist.")
		);

		if (event.getInitiator().getId().equals(userId))
			throw new DataConflictException("Event initiator cannot add a request.");

		if (!event.getState().equals(EventState.PUBLISHED))
			throw new DataConflictException("Event is unpublished.");

		int participantLimit = event.getParticipantLimit();
		int confirmedRequests = event.getConfirmedRequests();
		if (participantLimit != 0 && participantLimit == confirmedRequests)
			throw new DataConflictException("Participation limit reached.");

		ParticipationRequestEntity requestToSave = createRequest(user, event);
		ParticipationRequestEntity savedRequest = requestRepository.save(requestToSave);
		return ParticipationRequestMapper.entityToDto(savedRequest);
	}

	private ParticipationRequestEntity createRequest(UserEntity user, EventEntity event) {
		ParticipationRequestEntity request = new ParticipationRequestEntity();
		request.setRequester(user);
		request.setEvent(event);
		request.setCreatedOn(LocalDateTime.now());
		if (!event.getRequestModeration() || event.getParticipantLimit().equals(0)) {
			request.setStatus(ParticipationStatus.CONFIRMED);
			event.setConfirmedRequests(event.getConfirmedRequests() + 1);
			eventRepository.save(event);
		}
		else {
			request.setStatus(ParticipationStatus.PENDING);
		}
		return request;
	}

	@Override
	public ParticipationRequestDto cancelParticipationRequest(int userId, int requestId) throws DataNotFoundException {
		ParticipationRequestEntity request = requestRepository.findWithRequesterEventById(requestId).orElseThrow(
				() -> new DataNotFoundException("Request with id [" + requestId + "] doesn't exist.")
		);

		if (request.getRequester().getId() != userId)
			throw new DataNotFoundException("User is not requester.");

		EventEntity event = request.getEvent();

		if (request.getStatus().equals(ParticipationStatus.CONFIRMED)){
			request.setStatus(ParticipationStatus.CANCELED);
			event.setConfirmedRequests(event.getConfirmedRequests() - 1);
			eventRepository.save(event);
		}
		if (request.getStatus().equals(ParticipationStatus.PENDING)){
			request.setStatus(ParticipationStatus.CANCELED);
		}

		return ParticipationRequestMapper.entityToDto(requestRepository.save(request));
	}

	@Override
	public List<EventShortDto> getPublicEvents(PublicGetEventsRequest request, HttpServletRequest httpServletRequest) throws
			BadRequestException {

		if (request.getStart() != null && request.getEnd() != null)
			if (request.getStart().isAfter(request.getEnd()))
				throw new BadRequestException("Range start cannot be after range end.");

		List<EventEntity> events = eventRepository.findWithCategoryAndInitiatorBySearchParam(EventState.PUBLISHED,
				request.getText(), request.getStart(), request.getEnd(), request.getCategories(), request.getPaid(),
				request.getOnlyAvailable(), request.getPageable());

		List<EventShortDto> shortEvents = events.stream()
				.map(EventMapper::entityToShortDto)
				.collect(Collectors.toList());

		setViewsToEventShortDtos(shortEvents);

		sortEventsShortDto(shortEvents, request.getSort());

		sendHits(httpServletRequest,
				events.stream()
						.map(EventEntity::getId)
						.collect(Collectors.toList())
		);

		return shortEvents;
	}

	@Override
	public EventFullDto getEvent(int eventId, HttpServletRequest httpRequest) throws DataNotFoundException {
		EventEntity eventEntity = eventRepository.findWithCategoryInitiatorLocationByIdAndStateIn(eventId,
				List.of(EventState.PUBLISHED)).orElseThrow(
				() -> new DataNotFoundException("Event with id[" + eventId + "] doesn't exist.")
		);

		EventFullDto eventDto = EventMapper.entityToFullDto(eventEntity);

		eventDto.setViews(getEventViews(eventId));

		sendHit(httpRequest);

		return eventDto;
	}

	private void sortEventsShortDto(List<EventShortDto> events, EventSort sort) {
		if (sort != null) {
			if (sort.equals(EventSort.VIEWS))
				events.sort(Comparator.comparingLong(EventShortDto::getViews));
			if (sort.equals(EventSort.EVENT_DATE))
				events.sort(Comparator.comparing(EventShortDto::getEventDate));
		}
	}

	private void setNewInfoForEventToUpdateByAdmin(EventEntity event, UpdateEventAdminRequest request) {
		if (request.getAnnotation() != null)
			event.setAnnotation(request.getAnnotation());
		if (request.getCategory() != null) {
			CategoryEntity newCategory = categoryRepository.findById(request.getCategory()).orElseThrow(
					() -> new DataNotFoundException("Category with id[" + request.getCategory() + "] doesn't exist.")
			);
			event.setCategory(newCategory);
		}
		if (request.getDescription() != null)
			event.setDescription(request.getDescription());
		if (request.getEventDate() != null)
			event.setEventDate(request.getEventDate());
		if (request.getLocation() != null)
			event.setLocation(LocationMapper.dtoToEntity(request.getLocation()));
		if (request.getPaid() != null)
			event.setPaid(request.getPaid());
		if (request.getParticipantLimit() != null)
			event.setParticipantLimit(request.getParticipantLimit());
		if (request.getRequestModeration() != null)
			event.setRequestModeration(request.getRequestModeration());
		if (request.getStateAction() != null) {
			if (request.getStateAction().equals(EventStateActionAdmin.PUBLISH_EVENT))
				event.setState(EventState.PUBLISHED);
			if (request.getStateAction().equals(EventStateActionAdmin.REJECT_EVENT))
				event.setState(EventState.CANCELED);
		}
		if (request.getTitle() != null)
			event.setTitle(request.getTitle());
	}

	private void setNewStatusToParticipationRequests(EventEntity event, EventRequestStatusUpdateRequest updateRequest) {
		List<ParticipationRequestEntity> requests = event.getRequests();
		List<Integer> updateRequestIds = updateRequest.getRequestIds();
		ParticipationStatus newStatus = updateRequest.getStatus();
		requests.forEach(request -> {
			if (!request.getStatus().equals(ParticipationStatus.PENDING))
				throw new DataConflictException("Requests must have status PENDING." +
						" Request with id[" + request.getId() +"] have status[" + request.getStatus() +"].");

			if (updateRequestIds.contains(request.getId())) {
				if (newStatus.equals(ParticipationStatus.CONFIRMED)) {
					if (!event.getParticipantLimit().equals(0) && !event.getParticipantLimit().equals(event.getConfirmedRequests())) {
						request.setStatus(ParticipationStatus.CONFIRMED);
						event.setConfirmedRequests(event.getConfirmedRequests() + 1);
					} else {
						request.setStatus(ParticipationStatus.REJECTED);
					}
				} else if (newStatus.equals(ParticipationStatus.REJECTED)) {
					request.setStatus(ParticipationStatus.REJECTED);
				}
			} else if (!event.getParticipantLimit().equals(0) && event.getParticipantLimit().equals(event.getConfirmedRequests())) {
				request.setStatus(ParticipationStatus.REJECTED);
			}
		});
	}

	private void setNewInfoForEventToUpdateByUser(EventEntity event, UpdateEventUserRequest request) {
		if (request.getAnnotation() != null)
			event.setAnnotation(request.getAnnotation());
		if (request.getCategory() != null) {
			CategoryEntity newCategory = categoryRepository.findById(request.getCategory()).orElseThrow(
					() -> new DataNotFoundException("Category with id[" + request.getCategory() + "] doesn't exist.")
			);
			event.setCategory(newCategory);
		}
		if (request.getDescription() != null)
			event.setDescription(request.getDescription());
		if (request.getEventDate() != null)
			event.setEventDate(request.getEventDate());
		if (request.getLocation() != null)
			event.setLocation(LocationMapper.dtoToEntity(request.getLocation()));
		if (request.getPaid() != null)
			event.setPaid(request.getPaid());
		if (request.getParticipantLimit() != null)
			event.setParticipantLimit(request.getParticipantLimit());
		if (request.getRequestModeration() != null)
			event.setRequestModeration(request.getRequestModeration());
		if (request.getStateAction() != null) {
			if (request.getStateAction().equals(EventStateActionUser.CANCEL_REVIEW))
				event.setState(EventState.CANCELED);
			if (request.getStateAction().equals(EventStateActionUser.SEND_TO_REVIEW))
				event.setState(EventState.PENDING);
		}
		if (request.getTitle() != null)
			event.setTitle(request.getTitle());
	}

	private CategoryEntity getCategoryEntityOrThrowException(int categoryId) {
		return categoryRepository.findById(categoryId).orElseThrow(
				() -> new DataNotFoundException("Category with id[" + categoryId + "] doesn't exist.")
		);
	}

	private UserEntity getUserEntityOrThrowException(int userId) {
		return userRepository.findById(userId).orElseThrow(
				() -> new DataNotFoundException("User with id [" + userId + "] doesn't exist.")
		);
	}

	private void setViewsToEventFullDtos(List<EventFullDto> eventFullDtos) {
		if (eventFullDtos.isEmpty())
			return;
		List<Integer> eventsId = eventFullDtos.stream()
				.map(EventFullDto::getId)
				.collect(Collectors.toList());

		Map<Integer, Long> eventIdViews = getEventsViews(eventsId);

		eventFullDtos.forEach(
				event -> {
					Long views = eventIdViews.get(event.getId());
					if (views != null)
						event.setViews(views);
					else
						event.setViews(0L);
				}
		);
	}

	private void setViewsToEventShortDtos(List<EventShortDto> eventShortDtos) {
		if (eventShortDtos.isEmpty())
			return;
		List<Integer> eventsId = eventShortDtos.stream()
				.map(EventShortDto::getId)
				.collect(Collectors.toList());

		Map<Integer, Long> eventIdViews = getEventsViews(eventsId);

		eventShortDtos.forEach(
				event -> {
					Long views = eventIdViews.get(event.getId());
					if (views != null)
						event.setViews(views);
					else
						event.setViews(0L);
				}
		);
	}

	private Map<Integer, Long> getEventsViews(List<Integer> eventsId) {
		List<String> uris = eventsId.stream()
				.map(eventId -> "/events/" + eventId)
				.collect(Collectors.toList());

		List<StatDto> stats = statClient.getStats(webClient, LocalDateTime.now().minusYears(10),
				LocalDateTime.now().plusYears(10), uris, true);

		return stats.stream()
				.filter(stat -> stat.getApp().equals(appName))
				.collect(Collectors.toMap(
						stat -> getEventIdFromUri(stat.getUri()),
						StatDto::getHits)
				);
	}

	private Integer getEventIdFromUri(String uri) {
		//uri: /events/{eventId}
		return Integer.parseInt(uri.split("/")[2]);
	}

	private Long getEventViews(int eventId) {
		String uri = "/events/" + eventId;
		List<StatDto> stats = statClient.getStats(webClient, LocalDateTime.now().minusYears(100),
				LocalDateTime.now().plusYears(100), List.of(uri), true);
		if (stats.isEmpty())
			return 0L;
		Optional<StatDto> eventStat = stats.stream()
				.filter(statDto -> statDto.getApp().equals(appName))
				.findFirst();
		if (eventStat.isEmpty())
			return 0L;
		return eventStat.get().getHits();
	}

	private void sendHit(HttpServletRequest httpRequest) {
		String uri = httpRequest.getRequestURI();
		String requesterIp = httpRequest.getRemoteAddr();
		statClient.sendHitToSave(webClient, appName, uri, requesterIp, LocalDateTime.now());
	}

	private void sendHits(HttpServletRequest httpRequest, List<Integer> eventsId) {
		List<String> uris = eventsId.stream()
				.map(eventId -> "/events/" + eventId)
				.collect(Collectors.toList());
		String requesterIp = httpRequest.getRemoteAddr();
		LocalDateTime hitTime = LocalDateTime.now();
		uris.forEach(uri -> statClient.sendHitToSave(webClient, appName, uri, requesterIp, hitTime));
	}
}