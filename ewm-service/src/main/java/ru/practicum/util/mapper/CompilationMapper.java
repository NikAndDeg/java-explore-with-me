package ru.practicum.util.mapper;

import lombok.experimental.UtilityClass;
import ru.practicum.model.dto.event.EventShortDto;
import ru.practicum.model.dto.event.compilation.CompilationDto;
import ru.practicum.model.dto.event.compilation.NewCompilationDto;
import ru.practicum.model.entity.CompilationEntity;
import ru.practicum.model.entity.EventEntity;

import java.util.List;
import java.util.stream.Collectors;

@UtilityClass
public class CompilationMapper {

	public CompilationEntity newDtoToEntity(NewCompilationDto dto) {
		CompilationEntity entity = new CompilationEntity();
		entity.setTitle(dto.getTitle());
		entity.setPinned(dto.getPinned());
		return entity;
	}

	public CompilationDto entityToDto(CompilationEntity entity, List<EventEntity> events) {
		List<EventShortDto> eventShortDtos = events.stream()
				.map(EventMapper::entityToShortDto)
				.collect(Collectors.toList());

		return CompilationDto.builder()
				.id(entity.getId())
				.pinned(entity.getPinned())
				.title(entity.getTitle())
				.events(eventShortDtos)
				.build();
	}

	public CompilationDto entityToDtoWithoutEvents(CompilationEntity entity) {
		return CompilationDto.builder()
				.id(entity.getId())
				.pinned(entity.getPinned())
				.title(entity.getTitle())
				.events(List.of())
				.build();
	}
}
