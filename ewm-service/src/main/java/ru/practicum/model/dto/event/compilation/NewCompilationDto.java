package ru.practicum.model.dto.event.compilation;

import lombok.Data;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
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
