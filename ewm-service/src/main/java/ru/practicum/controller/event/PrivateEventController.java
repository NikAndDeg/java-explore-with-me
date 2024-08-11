package ru.practicum.controller.event;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.model.dto.event.EventFullDto;
import ru.practicum.model.dto.event.EventShortDto;
import ru.practicum.model.dto.event.NewEventDto;
import ru.practicum.model.dto.event.participation.ParticipationRequestDto;
import ru.practicum.model.dto.request.EventRequestStatusUpdateRequest;
import ru.practicum.model.dto.request.EventRequestStatusUpdateResult;
import ru.practicum.model.dto.request.UpdateEventUserRequest;
import ru.practicum.service.event.EventService;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Validated
@RestController
@RequestMapping(path = "/users/{userId}")
public class PrivateEventController {
	private final EventService eventService;

	//GET /users/{userId}/events
	@GetMapping("/events")
	public List<EventShortDto> getUserEvent(@PathVariable @Min(1) int userId,
											@RequestParam(defaultValue = "0") @Min(0) int from,
											@RequestParam(defaultValue = "10") @Min(1) int size) {
		log.info("Request to get user's events with userId[{}], from[{}], size[{}].", userId, from, size);
		List<EventShortDto> events = eventService.getUserEvents(userId, from, size);
		log.info("User's events received.");
		return events;
	}

	//POST /users/{userId}/events
	@ResponseStatus(HttpStatus.CREATED)
	@PostMapping("/events")
	public EventFullDto addEvent(@PathVariable @Min(1) int userId,
								 @RequestBody @Valid NewEventDto newEventDto) {
		log.info("Request to add event with userId[{}].", userId);
		EventFullDto addedEvent = eventService.addEvent(userId, newEventDto);
		log.info("Event[id[{}], title[{}]] added.", addedEvent.getId(), addedEvent.getTitle());
		return addedEvent;
	}

	//GET /users/{userId}/events/{eventId}
	@GetMapping("/events/{eventId}")
	public EventFullDto getUserEvent(@PathVariable @Min(1) int userId,
									 @PathVariable @Min(1) int eventId) {
		log.info("Request to get user's event with userId[{}], eventId[{}].", userId, eventId);
		EventFullDto event = eventService.getUserEvent(userId, eventId);
		log.info("Event[id[{}], title[{}]] received.", event.getId(), event.getTitle());
		return event;
	}

	//PATCH /users/{userId}/events/{eventId}
	@PatchMapping("/events/{eventId}")
	public EventFullDto updateEvent(@PathVariable @Min(1) int userId,
									@PathVariable @Min(1) int eventId,
									@RequestBody @Valid UpdateEventUserRequest updateRequest) {
		log.info("Request to update Event with userId[{}], eventId[{}].", userId, eventId);
		EventFullDto updatedEvent = eventService.updateEvent(userId, eventId, updateRequest);
		log.info("Event[id[{}], title[{}]] updated.", updatedEvent.getId(), updatedEvent.getTitle());
		return updatedEvent;
	}

	//GET /users/{userId}/events/{eventId}/requests
	@GetMapping("events/{eventId}/requests")
	public List<ParticipationRequestDto> getParticipationRequests(@PathVariable @Min(1) int userId,
																  @PathVariable @Min(1) int eventId) {
		log.info("Request to get participation requests with userId[{}], eventId[{}].", userId, eventId);
		List<ParticipationRequestDto> requests = eventService.getParticipationRequests(userId, eventId);
		log.info("Participation requests[{}] received.", requests);
		return requests;
	}

	//PATCH /users/{userId}/events/{eventId}/requests
	@PatchMapping("/events/{eventId}/requests")
	public EventRequestStatusUpdateResult updateEventRequestStatus(@PathVariable @Min(1) int userId,
																   @PathVariable @Min(1) int eventId,
																   @RequestBody EventRequestStatusUpdateRequest updateRequest) {
		log.info("Request to update event request status with userId[{}], eventId[{}], update request[{}].",
				userId, eventId, updateRequest);
		EventRequestStatusUpdateResult result = eventService.updateEventRequestStatus(userId, eventId, updateRequest);
		log.info("Requests updated.");
		return result;
	}

	//GET /users/{userId}/requests
	@GetMapping("/requests")
	public List<ParticipationRequestDto> getUserParticipationRequests(@PathVariable @Min(1) int userId) {
		log.info("Request to get user's participation requests with userId[{}].", userId);
		List<ParticipationRequestDto> userParticipationRequests = eventService.getUserParticipationRequests(userId);
		log.info("User's participation requests received.");
		return userParticipationRequests;
	}

	//POST /users/{userId}/requests
	@ResponseStatus(HttpStatus.CREATED)
	@PostMapping("/requests")
	public ParticipationRequestDto addParticipationRequest(@PathVariable @Min(1) int userId,
														   @RequestParam @Min(1) int eventId) {
		log.info("Request to add participation request with userId[{}], eventId[{}].", userId, eventId);
		ParticipationRequestDto addedRequest = eventService.addParticipationRequest(userId, eventId);
		log.info("Participation request[{}] added.", addedRequest);
		return addedRequest;
	}

	//PATCH /users/{userId}/requests/{requestId}/cancel
	@PatchMapping("/requests/{requestId}/cancel")
	public ParticipationRequestDto cancelParticipationRequest(@PathVariable @Min(1) int userId,
															  @PathVariable @Min(1) int requestId) {
		log.info("Request to cancel participation request with userId[{}], requestId[{}].", userId, requestId);
		ParticipationRequestDto canceledRequest = eventService.cancelParticipationRequest(userId, requestId);
		log.info("Participation request[{}] canceled.", canceledRequest);
		return canceledRequest;
	}
}