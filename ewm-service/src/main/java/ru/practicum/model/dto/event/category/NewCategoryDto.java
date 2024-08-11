package ru.practicum.model.dto.event.category;

import lombok.Data;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

@Data
public class NewCategoryDto {
	@NotNull
	@NotBlank
	@Size(min = 1, max = 50)
	private String name;
}
