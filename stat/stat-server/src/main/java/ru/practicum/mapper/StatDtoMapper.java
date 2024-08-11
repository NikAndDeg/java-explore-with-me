package ru.practicum.mapper;

import lombok.experimental.UtilityClass;
import ru.practicum.StatDto;
import ru.practicum.repository.entity.Stat;

@UtilityClass
public class StatDtoMapper {
	public static StatDto statModelToDto(Stat stat) {
		StatDto statDto = new StatDto();
		statDto.setApp(stat.getApp());
		statDto.setUri(stat.getUri());
		statDto.setHits(stat.getHits());
		return statDto;
	}
}
