package ru.practicum.service.comment;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.exception.BadRequestException;
import ru.practicum.exception.DataConflictException;
import ru.practicum.exception.DataNotFoundException;
import ru.practicum.model.dto.comment.CommentDto;
import ru.practicum.model.dto.comment.DeletedCommentDto;
import ru.practicum.model.dto.comment.NewCommentDto;
import ru.practicum.model.entity.CommentEntity;
import ru.practicum.model.entity.EventEntity;
import ru.practicum.model.entity.UserEntity;
import ru.practicum.model.event.EventState;
import ru.practicum.repostitory.CommentRepository;
import ru.practicum.repostitory.EventRepository;
import ru.practicum.repostitory.UserRepository;
import ru.practicum.util.Pagenator;
import ru.practicum.util.mapper.CommentMapper;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class CommentServiceImpl implements CommentService {
	private final CommentRepository commentRepository;
	private final UserRepository userRepository;
	private final EventRepository eventRepository;

	@Override
	@Transactional
	public CommentDto updateCommentByAdmin(int commentId, NewCommentDto newCommentDto) throws DataNotFoundException {
		CommentEntity commentToUpdate = commentRepository.findWithCommenterById(commentId).orElseThrow(
				() -> new DataNotFoundException("Comment with id[" + commentId + " doesn't exist.")
		);

		setNewInfoForCommentByAdmin(commentToUpdate, newCommentDto);

		return CommentMapper.entityToDto(commentRepository.save(commentToUpdate), commentToUpdate.getCommenter());
	}

	@Override
	@Transactional
	public DeletedCommentDto deleteCommentByAdmin(int commentId) throws DataNotFoundException {
		CommentEntity commentToDelete = commentRepository.findById(commentId).orElseThrow(
				() -> new DataNotFoundException("Comment with id[" + commentId + " doesn't exist.")
		);

		commentRepository.delete(commentToDelete);

		return CommentMapper.entityToDeletedDto(commentToDelete);
	}

	@Override
	@Transactional
	public CommentDto getCommentByAdmin(int commentId) throws DataNotFoundException {
		CommentEntity comment = commentRepository.findWithCommenterById(commentId).orElseThrow(
				() -> new DataNotFoundException("Comment with id[" + commentId + " doesn't exist.")
		);

		return CommentMapper.entityToDto(comment, comment.getCommenter());
	}

	@Override
	@Transactional
	public List<CommentDto> getCommentsByAdmin(int eventId, String text, LocalDateTime start, LocalDateTime end,
											   int from, int size) throws DataNotFoundException, BadRequestException {
		if (start != null && end != null)
			if (start.isAfter(end))
				throw new BadRequestException("Range start cannot be after range end.");

		eventRepository.findById(eventId).orElseThrow(
				() -> new DataNotFoundException("Event with id[" + eventId + "] doesn't exist.")
		);

		List<CommentEntity> comments = commentRepository.findWithCommenterBySearchParam(eventId, text, start, end,
				Pagenator.getPageable(from, size));

		List<CommentDto> commentsDto = comments.stream()
				.map(comment -> CommentMapper.entityToDto(comment, comment.getCommenter()))
				.toList();

		sortCommentsByCreatedOn(commentsDto);

		return commentsDto;
	}

	@Override
	@Transactional
	public CommentDto addComment(int userId, int eventId, NewCommentDto newCommentDto) throws DataNotFoundException,
			DataConflictException {
		EventEntity event = eventRepository.findById(eventId).orElseThrow(
				() -> new DataNotFoundException("Event with id[" + eventId + "] doesn't exist.")
		);

		if (event.getInitiator().getId().equals(userId))
			throw new DataConflictException("Event initiator cannot add a comment.");

		if (!event.getState().equals(EventState.PUBLISHED))
			throw new DataConflictException("Event is unpublished.");

		UserEntity commenter = userRepository.findById(userId).orElseThrow(
				() -> new DataNotFoundException("User with id[" + userId + "] doesn't exist.")
		);

		CommentEntity commentToSave = CommentMapper.newCommentDtoToEntity(commenter, event, newCommentDto);

		return CommentMapper.entityToDto(commentRepository.save(commentToSave), commenter);
	}

	@Override
	@Transactional
	public DeletedCommentDto deleteCommentByUser(int userId, int commentId) throws DataNotFoundException,
			DataConflictException {
		UserEntity commenter = userRepository.findById(userId).orElseThrow(
				() -> new DataNotFoundException("User with id[" + userId + "] doesn't exist.")
		);

		CommentEntity commentToDelete = commentRepository.findWithCommenterById(commentId).orElseThrow(
				() -> new DataNotFoundException("Comment with id[" + commentId + "] doesn't exist.")
		);

		if (!commentToDelete.getCommenter().getId().equals(commenter.getId()))
			throw new DataConflictException("User is not commenter.");

		commentRepository.delete(commentToDelete);

		return CommentMapper.entityToDeletedDto(commentToDelete);
	}

	@Override
	@Transactional
	public CommentDto getCommentByUser(int userId, int commentId) throws DataNotFoundException {
		userRepository.findById(userId).orElseThrow(
				() -> new DataNotFoundException("User with id[" + userId + "] doesn't exist.")
		);

		CommentEntity comment = commentRepository.findWithCommenterById(commentId).orElseThrow(
				() -> new DataNotFoundException("Comment with id[" + commentId + "] doesn't exist.")
		);

		return CommentMapper.entityToDto(comment, comment.getCommenter());
	}

	@Override
	@Transactional
	public List<CommentDto> getCommentsByUser(int userId, int eventId, String text, LocalDateTime start,
											  LocalDateTime end, int from, int size) throws DataNotFoundException, BadRequestException {
		if (start != null && end != null)
			if (start.isAfter(end))
				throw new BadRequestException("Range start cannot be after range end.");

		userRepository.findById(userId).orElseThrow(
				() -> new DataNotFoundException("User with id [" + userId + "]  doesn't exist.")
		);

		eventRepository.findById(eventId).orElseThrow(
				() -> new DataNotFoundException("Event with id[" + eventId + "] doesn't exist.")
		);

		List<CommentEntity> comments = commentRepository.findWithCommenterBySearchParam(eventId, text, start, end,
				Pagenator.getPageable(from, size));

		List<CommentDto> commentsDto = comments.stream()
				.map(comment -> CommentMapper.entityToDto(comment, comment.getCommenter()))
				.toList();

		sortCommentsByCreatedOn(commentsDto);

		return commentsDto;
	}

	private void setNewInfoForCommentByAdmin(CommentEntity commentToUpdate, NewCommentDto newCommentDto) {
		commentToUpdate.setText(newCommentDto.getText());
	}

	private void sortCommentsByCreatedOn(List<CommentDto> comments) {
		if (comments == null || comments.isEmpty() || comments.size() < 2)
			return;
		comments.sort(Comparator.comparing(CommentDto::getCreatedOn));
	}
}
