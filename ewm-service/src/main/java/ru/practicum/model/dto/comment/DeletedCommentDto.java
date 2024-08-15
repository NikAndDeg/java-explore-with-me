package ru.practicum.model.dto.comment;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class DeletedCommentDto {
	private Integer id;
	private Integer eventId;
	private Integer userId;
	private String text;
	private LocalDateTime createdOn;
}
