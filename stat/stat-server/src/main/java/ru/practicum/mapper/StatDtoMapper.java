package ru.practicum.mapper;

import lombok.experimental.UtilityClass;
import ru.practicum.StatDto;
import ru.practicum.repository.entity.Stat;

@UtilityClass
public class StatDtoMapper {
	public static StatDto statModelToDto(Stat stat) {
		return StatDto.builder()
				.app(stat.getApp())
				.uri(stat.getUri())
				.hits(stat.getHits())
				.build();
	}
}
