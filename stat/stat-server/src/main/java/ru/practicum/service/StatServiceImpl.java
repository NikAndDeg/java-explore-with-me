package ru.practicum.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.HitDto;
import ru.practicum.StatDto;
import ru.practicum.exception.BadRequestException;
import ru.practicum.mapper.HitDtoMapper;
import ru.practicum.mapper.StatDtoMapper;
import ru.practicum.repository.entity.Hit;
import ru.practicum.repository.entity.Stat;
import ru.practicum.repository.HitRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class StatServiceImpl implements StatService {
	private final HitRepository repository;

	@Override
	@Transactional
	public HitDto saveHit(HitDto hitDto) {
		Hit savedHit = repository.save(HitDtoMapper.hitDtoToModel(hitDto));
		return HitDtoMapper.hitModelToDto(savedHit);
	}

	@Override
	@Transactional
	public List<StatDto> getStat(LocalDateTime start, LocalDateTime end, List<String> uris, Boolean unique) {
		if (start != null && end != null)
			if (start.isAfter(end))
				throw new BadRequestException("Range start cannot be after range end.");

		List<Stat> stats;
		if (unique) {
			if (uris == null || uris.isEmpty())
				stats = repository.findStatDistinct(start, end);
			else
				stats = repository.findStatWithUrisDistinct(start, end, uris);
		} else {
			if (uris == null || uris.isEmpty())
				stats = repository.findStat(start, end);
			else
				stats = repository.findStatWithUris(start, end, uris);
		}
		return stats.stream()
				.map(StatDtoMapper::statModelToDto)
				.collect(Collectors.toList());
	}
}
