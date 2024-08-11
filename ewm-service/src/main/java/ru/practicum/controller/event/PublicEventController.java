package ru.practicum.controller.event;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.model.dto.event.EventFullDto;
import ru.practicum.model.dto.event.EventShortDto;
import ru.practicum.model.dto.request.PublicGetEventsRequest;
import ru.practicum.model.event.EventSort;
import ru.practicum.service.event.EventService;
import ru.practicum.util.Pagenator;

import javax.servlet.http.HttpServletRequest;
import javax.validation.constraints.Min;
import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Validated
@RestController
@RequestMapping(path = "/events")
public class PublicEventController {
	private final EventService eventService;
	private static final String dateTimePattern = "yyyy-MM-dd HH:mm:ss";

	//GET /events
	@GetMapping
	public List<EventShortDto> getEvents(@RequestParam(required = false) String text,
										 @RequestParam(required = false) List<Integer> categories,
										 @RequestParam(required = false) Boolean paid,
										 @RequestParam(required = false) @DateTimeFormat(pattern = dateTimePattern) LocalDateTime rangeStart,
										 @RequestParam(required = false) @DateTimeFormat(pattern = dateTimePattern) LocalDateTime rangeEnd,
										 @RequestParam(defaultValue = "false") boolean onlyAvailable,
										 @RequestParam(required = false) EventSort sort,
										 @RequestParam(defaultValue = "0") @Min(0) int from,
										 @RequestParam(defaultValue = "10") @Min(1) int size,
										 HttpServletRequest httpServletRequest) {
		log.info("Request to get events with text[{}], categories[{}], paid[{}], start[{}], end[{}], onlyAvailable[{}], " +
				"sort[{}], from[{}], size[{}].", text, categories, paid, rangeStart, rangeEnd, onlyAvailable, sort, from, size);
		PublicGetEventsRequest request = new PublicGetEventsRequest(text, categories, paid, rangeStart, rangeEnd,
				onlyAvailable, sort, Pagenator.getPageable(from, size));
		List<EventShortDto> events = eventService.getPublicEvents(request, httpServletRequest);
		log.info("Events received.");
		return events;
	}

	//GET /events/{id}
	@GetMapping("/{eventId}")
	public EventFullDto getEvent(@PathVariable @Min(1) int eventId, HttpServletRequest httpRequest) {
		log.info("Request to get event with eventId[{}] by client with ip[{}].", eventId, httpRequest.getRemoteAddr());
		EventFullDto event = eventService.getEvent(eventId, httpRequest);
		log.info("Event[id[{}], title[{}]] received.", event.getId(), event.getTitle());
		return event;
	}
}