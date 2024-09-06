package ru.practicum.controller.comment;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.model.dto.comment.CommentDto;
import ru.practicum.model.dto.comment.DeletedCommentDto;
import ru.practicum.model.dto.comment.NewCommentDto;
import ru.practicum.service.comment.CommentService;

import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Validated
@RestController
@RequestMapping(path = "/users/{userId}/comments")
public class PrivateCommentController {
	private final CommentService commentService;
	private static final String dateTimePattern = "yyyy-MM-dd HH:mm:ss";

	//POST /users/{userId}/comments/{eventId}
	@ResponseStatus(HttpStatus.CREATED)
	@PostMapping("/{eventId}")
	public CommentDto addComment(@PathVariable @Min(1) int userId,
								 @PathVariable @Min(1) int eventId,
								 @RequestBody @Valid NewCommentDto newCommentDto) {
		log.info("Request to add comment with userId[{}], eventId[{}], comment[{}].",
				userId, eventId, newCommentDto);
		CommentDto addedComment = commentService.addComment(userId, eventId, newCommentDto);
		log.info("Comment[{}] added.", addedComment);
		return addedComment;
	}

	//DELETE /users/{userId}/comments/{commentId}
	@ResponseStatus(HttpStatus.NO_CONTENT)
	@DeleteMapping("/{commentId}")
	public DeletedCommentDto deleteComment(@PathVariable @Min(1) int userId,
										   @PathVariable @Min(1) int commentId) {
		log.info("Request to delete comment with userId[{}], commentId[{}].", userId, commentId);
		DeletedCommentDto deletedComment = commentService.deleteCommentByUser(userId, commentId);
		log.info("Comment[{}] deleted.", deletedComment);
		return deletedComment;
	}

	//GET /users/{userId}/comments/{commentId}
	@GetMapping("/{commentId}")
	public CommentDto getComment(@PathVariable @Min(1) int userId,
								 @PathVariable @Min(1) int commentId) {
		log.info("Request to get comment with userId[{}], commentId[{}].", userId, commentId);
		CommentDto commentDto = commentService.getCommentByUser(userId, commentId);
		log.info("Comment[{}] received.", commentDto);
		return commentDto;
	}

	//GET /users/{userId}/comments
	@GetMapping
	public List<CommentDto> getComments(@PathVariable @Min(1) int userId,
										@RequestParam @Min(1) int eventId,
										@RequestParam(required = false) String text,
										@RequestParam(required = false) @DateTimeFormat(pattern = dateTimePattern) LocalDateTime rangeStart,
										@RequestParam(required = false) @DateTimeFormat(pattern = dateTimePattern) LocalDateTime rangeEnd,
										@RequestParam(defaultValue = "0") @Min(0) int from,
										@RequestParam(defaultValue = "10") @Min(1) int size) {
		log.info("Request to get comments with userId[{}], eventId[{}], text[{}], start[{}], end[{}], from[{}], size[{}].",
				userId, eventId, text, rangeStart, rangeEnd, from, size);
		List<CommentDto> comments = commentService.getCommentsByUser(userId, eventId, text, rangeStart, rangeEnd, from, size);
		log.info("Comments received.");
		return comments;
	}
}
