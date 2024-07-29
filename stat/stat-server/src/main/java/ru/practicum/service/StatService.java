package ru.practicum.service;

import ru.practicum.HitDto;
import ru.practicum.StatDto;

import java.time.LocalDateTime;
import java.util.List;

public interface StatService {
	HitDto saveHit(HitDto hitDto);

	List<StatDto> getStat(LocalDateTime start, LocalDateTime end, List<String> uris, Boolean unique);
}
