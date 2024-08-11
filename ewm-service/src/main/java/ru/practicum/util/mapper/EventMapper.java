package ru.practicum.util.mapper;

import lombok.experimental.UtilityClass;
import ru.practicum.model.dto.event.EventFullDto;
import ru.practicum.model.dto.event.EventShortDto;
import ru.practicum.model.dto.event.NewEventDto;
import ru.practicum.model.entity.EventEntity;
import ru.practicum.model.entity.UserEntity;
import ru.practicum.model.event.EventState;

import java.time.LocalDateTime;

@UtilityClass
public class EventMapper {
	public static EventFullDto entityToFullDto(EventEntity entity) {
		return EventFullDto.builder()
				.annotation(entity.getAnnotation())
				.category(CategoryMapper.entityToDto(entity.getCategory()))
				.confirmedRequests(entity.getConfirmedRequests())
				.createdOn(entity.getCreatedOn())
				.description(entity.getDescription())
				.eventDate(entity.getEventDate())
				.id(entity.getId())
				.initiator(UserMapper.entityToShortDto(entity.getInitiator()))
				.location(LocationMapper.entityToDto(entity.getLocation()))
				.paid(entity.getPaid())
				.participantLimit(entity.getParticipantLimit())
				.publishedOn(entity.getPublishedOn())
				.requestModeration(entity.getRequestModeration())
				.state(entity.getState())
				.title(entity.getTitle())
				.build();
	}

	public EventShortDto entityToShortDto(EventEntity entity, UserEntity userEntity) {
		return EventShortDto.builder()
				.id(entity.getId())
				.annotation(entity.getAnnotation())
				.category(CategoryMapper.entityToDto(entity.getCategory()))
				.confirmedRequests(entity.getConfirmedRequests())
				.eventDate(entity.getEventDate())
				.initiator(UserMapper.entityToShortDto(userEntity))
				.paid(entity.getPaid())
				.title(entity.getTitle())
				.views(0L)
				.build();
	}

	public EventShortDto entityToShortDto(EventEntity entity) {
		return EventShortDto.builder()
				.id(entity.getId())
				.annotation(entity.getAnnotation())
				.category(CategoryMapper.entityToDto(entity.getCategory()))
				.confirmedRequests(entity.getConfirmedRequests())
				.eventDate(entity.getEventDate())
				.initiator(UserMapper.entityToShortDto(entity.getInitiator()))
				.paid(entity.getPaid())
				.title(entity.getTitle())
				.views(0L)
				.build();
	}

	public EventEntity newEventDtoToEntity(NewEventDto dto) {
		EventEntity entity = new EventEntity();
		entity.setAnnotation(dto.getAnnotation());
		entity.setDescription(dto.getDescription());
		entity.setEventDate(dto.getEventDate());
		entity.setPaid(dto.getPaid());
		entity.setParticipantLimit(dto.getParticipantLimit());
		entity.setRequestModeration(dto.getRequestModeration());
		entity.setTitle(dto.getTitle());
		entity.setState(EventState.PENDING);
		entity.setConfirmedRequests(0);
		entity.setCreatedOn(LocalDateTime.now());
		return entity;
	}
}
