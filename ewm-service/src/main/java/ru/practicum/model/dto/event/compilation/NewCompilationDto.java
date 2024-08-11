package ru.practicum.model.dto.event.compilation;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.List;

@Data
public class NewCompilationDto {
	private List<Integer> events;
	private Boolean pinned = false;
	@NotNull
	@NotBlank
	@Size(min = 1, max = 50)
	private String title;
}
