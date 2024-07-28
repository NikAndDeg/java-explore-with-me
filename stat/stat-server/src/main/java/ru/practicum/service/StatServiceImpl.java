package ru.practicum.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.HitDto;
import ru.practicum.StatDto;
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
		Hit savedHit = repository.save(hitDtoToModel(hitDto));
		return hitModelToDto(savedHit);
	}

	@Override
	public List<StatDto> getStat(LocalDateTime start, LocalDateTime end, List<String> uris, Boolean unique) {
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
				.map(this::statModelToDto)
				.collect(Collectors.toList());
	}

	private StatDto statModelToDto(Stat stat) {
		return StatDto.builder()
				.app(stat.getApp())
				.uri(stat.getUri())
				.hits(stat.getHits())
				.build();
	}

	private Hit hitDtoToModel(HitDto hitDto) {
		Hit hit = new Hit();
		hit.setApp(hitDto.getApp());
		hit.setUri(hitDto.getUri());
		hit.setIp(hitDto.getIp());
		hit.setTimestamp(hitDto.getTimestamp());
		return hit;
	}

	private HitDto hitModelToDto(Hit hit) {
		return HitDto.builder()
				.id(hit.getId())
				.app(hit.getApp())
				.uri(hit.getUri())
				.ip(hit.getIp())
				.timestamp(hit.getTimestamp())
				.build();
	}
}
