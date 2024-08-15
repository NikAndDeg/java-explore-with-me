package ru.practicum.util.mapper;

import lombok.experimental.UtilityClass;
import ru.practicum.model.dto.comment.CommentDto;
import ru.practicum.model.dto.comment.DeletedCommentDto;
import ru.practicum.model.dto.comment.NewCommentDto;
import ru.practicum.model.entity.CommentEntity;
import ru.practicum.model.entity.EventEntity;
import ru.practicum.model.entity.UserEntity;

import java.time.LocalDateTime;

@UtilityClass
public class CommentMapper {
	public static CommentDto entityToDto(CommentEntity entity, UserEntity commenter) {
		CommentDto dto = new CommentDto();
		dto.setId(entity.getId());
		dto.setEventId(entity.getEvent().getId());
		dto.setText(entity.getText());
		dto.setCreatedOn(entity.getCreatedOn());
		dto.setCommenter(UserMapper.entityToShortDto(commenter));
		return dto;
	}

	public static DeletedCommentDto entityToDeletedDto(CommentEntity entity) {
		DeletedCommentDto deletedDto = new DeletedCommentDto();
		deletedDto.setId(entity.getId());
		deletedDto.setEventId(entity.getEvent().getId());
		deletedDto.setUserId(entity.getCommenter().getId());
		deletedDto.setText(entity.getText());
		deletedDto.setText(entity.getText());
		return deletedDto;
	}

	public static CommentEntity newCommentDtoToEntity(UserEntity commenter, EventEntity event, NewCommentDto newCommentDto) {
		CommentEntity comment = new CommentEntity();
		comment.setEvent(event);
		comment.setCommenter(commenter);
		comment.setText(newCommentDto.getText());
		comment.setCreatedOn(LocalDateTime.now());
		return comment;
	}
}
