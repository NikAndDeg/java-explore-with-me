package ru.practicum.controller;

import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;
import ru.practicum.HitDto;
import ru.practicum.StatDto;
import ru.practicum.service.StatService;

import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
public class StatController {
	private final StatService service;
	public static final String dateTimePattern = "yyyy-MM-dd HH:mm:ss";

	@PostMapping("/hit")
	public HitDto saveHit(@RequestBody @Valid HitDto hitDto) {
		log.info("Request to save hit[{}].", hitDto);
		HitDto savedHitDto = service.saveHit(hitDto);
		log.info("Hit [{}] is saved.", savedHitDto);
		return savedHitDto;
	}

	@GetMapping("/stats")
	public List<StatDto> getStats(@RequestParam @DateTimeFormat(pattern = dateTimePattern) LocalDateTime start,
								  @RequestParam @DateTimeFormat(pattern = dateTimePattern) LocalDateTime end,
								  @RequestParam(required = false) List<String> uris,
								  @RequestParam(defaultValue = "false") Boolean unique) {
		log.info("Request to get stat with start[{}], end [{}] uris[{}], unique [{}].", start, end, uris, unique);
		List<StatDto> stats = service.getStat(start, end, uris, unique);
		log.info("Stat received.");
		return stats;
	}
}
