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
@RequestMapping(path = "/admin/comments")
public class AdminCommentController {
	private final CommentService commentService;
	private static final String dateTimePattern = "yyyy-MM-dd HH:mm:ss";

	//PATCH /admin/comments/{commentId}
	@PatchMapping("/{commentId}")
	public CommentDto updateComment(@PathVariable @Min(1) int commentId,
									@RequestBody @Valid NewCommentDto newCommentDto) {
		log.info("Request to update comment by admin with commentId[{}], newCommentDto[{}].",
				commentId, newCommentDto);
		CommentDto updatedComment = commentService.updateCommentByAdmin(commentId, newCommentDto);
		log.info("Comment[{}] updated by admin.", updatedComment);
		return updatedComment;
	}

	//DELETE /admin/comments/{commentId}
	@ResponseStatus(HttpStatus.NO_CONTENT)
	@DeleteMapping("/{commentId}")
	public DeletedCommentDto deleteComment(@PathVariable @Min(1) int commentId) {
		log.info("Request to delete comment by admin with commentId[{}].", commentId);
		DeletedCommentDto deletedComment = commentService.deleteCommentByAdmin(commentId);
		log.info("Comment[{}] deleted by admin.", deletedComment);
		return deletedComment;
	}

	//GET /admin/comments/{commentId}
	@GetMapping("/{commentId}")
	public CommentDto getComment(@PathVariable @Min(1) int commentId) {
		log.info("Request to get comment by admin by commentId[{}].", commentId);
		CommentDto comment = commentService.getCommentByAdmin(commentId);
		log.info("Comment[{}] received.", comment);
		return comment;
	}

	//GET /admin/comments/?eventId=int&from=int&size=int
	@GetMapping()
	public List<CommentDto> getComments(@RequestParam @Min(1) int eventId,
										@RequestParam(required = false) String text,
										@RequestParam(required = false) @DateTimeFormat(pattern = dateTimePattern) LocalDateTime rangeStart,
										@RequestParam(required = false) @DateTimeFormat(pattern = dateTimePattern) LocalDateTime rangeEnd,
										@RequestParam(defaultValue = "0") @Min(0) int from,
										@RequestParam(defaultValue = "10") @Min(1) int size) {
		log.info("Request to get comments by admin with eventId[{}], text[{}], start[{}], end[{}], from[{}], size[{}].",
				eventId, text, rangeStart, rangeEnd, from, size);
		List<CommentDto> comments = commentService.getCommentsByAdmin(eventId, text, rangeStart, rangeEnd, from, size);
		log.info("Comments received.");
		return comments;
	}
}
