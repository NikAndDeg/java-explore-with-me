package ru.practicum.mapper;

import lombok.experimental.UtilityClass;
import ru.practicum.HitDto;
import ru.practicum.repository.entity.Hit;

@UtilityClass
public class HitDtoMapper {
	public static Hit hitDtoToModel(HitDto hitDto) {
		Hit hit = new Hit();
		hit.setApp(hitDto.getApp());
		hit.setUri(hitDto.getUri());
		hit.setIp(hitDto.getIp());
		hit.setTimestamp(hitDto.getTimestamp());
		return hit;
	}

	public static HitDto hitModelToDto(Hit hit) {
		return HitDto.builder()
				.id(hit.getId())
				.app(hit.getApp())
				.uri(hit.getUri())
				.ip(hit.getIp())
				.timestamp(hit.getTimestamp())
				.build();
	}
}
