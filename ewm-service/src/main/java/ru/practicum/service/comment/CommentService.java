package ru.practicum.service.comment;

import ru.practicum.exception.BadRequestException;
import ru.practicum.exception.DataConflictException;
import ru.practicum.exception.DataNotFoundException;
import ru.practicum.model.dto.comment.CommentDto;
import ru.practicum.model.dto.comment.DeletedCommentDto;
import ru.practicum.model.dto.comment.NewCommentDto;

import java.time.LocalDateTime;
import java.util.List;

public interface CommentService {
	CommentDto updateCommentByAdmin(int commentId, NewCommentDto newCommentDto) throws DataNotFoundException;

	DeletedCommentDto deleteCommentByAdmin(int commentId) throws DataNotFoundException;

	CommentDto getCommentByAdmin(int commentId) throws DataNotFoundException;

	List<CommentDto> getCommentsByAdmin(int eventId, String text, LocalDateTime start, LocalDateTime end,
										int from, int size) throws DataNotFoundException, BadRequestException;

	CommentDto addComment(int userId, int eventId, NewCommentDto newCommentDto) throws DataNotFoundException;

	DeletedCommentDto deleteCommentByUser(int userId, int commentId) throws DataNotFoundException, DataConflictException;

	CommentDto getCommentByUser(int userId, int commentId) throws DataNotFoundException;

	List<CommentDto> getCommentsByUser(int userId, int eventId, String text, LocalDateTime start, LocalDateTime end,
									   int from, int size) throws DataNotFoundException, BadRequestException;
}
