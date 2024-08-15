package ru.practicum.controller.event;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.model.dto.event.EventFullDto;
import ru.practicum.model.dto.request.GetEventsServiceRequest;
import ru.practicum.model.dto.request.UpdateEventAdminRequest;
import ru.practicum.model.event.EventState;
import ru.practicum.service.event.EventService;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Validated
@RestController
@RequestMapping(path = "/admin/events")
public class AdminEventController {
	private final EventService eventService;
	private static final String dateTimePattern = "yyyy-MM-dd HH:mm:ss";

	//GET /admin/events
	@GetMapping
	public List<EventFullDto> getEvents(@RequestParam(required = false) List<Integer> users,
										@RequestParam(required = false) List<EventState> states,
										@RequestParam(required = false) List<Integer> categories,
										@RequestParam(required = false) @DateTimeFormat(pattern = dateTimePattern) LocalDateTime rangeStart,
										@RequestParam(required = false) @DateTimeFormat(pattern = dateTimePattern) LocalDateTime rangeEnd,
										@RequestParam(defaultValue = "0") @Min(0) int from,
										@RequestParam(defaultValue = "10") @Min(1) int size) {
		log.info("Request to get events with usersId[{}], states[{}], categoriesId[{}], start[{}], end [{}], " +
				"from[{}], size[{}].", users, states, categories, rangeStart, rangeEnd, from, size);
		GetEventsServiceRequest getEventsRequest = new GetEventsServiceRequest(users, states, categories,
				rangeStart, rangeEnd, from, size);
		List<EventFullDto> events = eventService.getEvents(getEventsRequest);
		log.info("Events received.");
		return events;
	}

	//PATCH /admin/events/{eventId}
	@PatchMapping("/{eventId}")
	public EventFullDto updateEvent(@PathVariable @Min(1) int eventId,
									@RequestBody @Valid UpdateEventAdminRequest request) {
		log.info("Request to update event with eventId[{}], request[{}].", eventId, request);
		EventFullDto updatedEvent = eventService.updateEvent(eventId, request);
		log.info("Event[id[{}], title[{}]] updated.", updatedEvent.getId(), updatedEvent.getTitle());
		return updatedEvent;
	}
}
