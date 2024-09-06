package ru.practicum.model.dto.comment;

import lombok.Data;
import ru.practicum.model.dto.user.UserShortDto;

import java.time.LocalDateTime;

@Data
public class CommentDto {
	private Integer id;
	private Integer eventId;
	private String text;
	private LocalDateTime createdOn;
	private UserShortDto commenter;
}
