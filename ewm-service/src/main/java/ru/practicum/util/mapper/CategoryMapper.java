package ru.practicum.util.mapper;

import lombok.experimental.UtilityClass;
import ru.practicum.model.dto.event.category.CategoryDto;
import ru.practicum.model.dto.event.category.NewCategoryDto;
import ru.practicum.model.entity.CategoryEntity;

@UtilityClass
public class CategoryMapper {
	public static CategoryEntity newCategoryDtoToEntity(NewCategoryDto dto) {
		CategoryEntity categoryEntity = new CategoryEntity();
		categoryEntity.setName(dto.getName());
		return categoryEntity;
	}

	public static CategoryDto entityToDto(CategoryEntity entity) {
		CategoryDto dto = new CategoryDto();
		dto.setId(entity.getId());
		dto.setName(entity.getName());
		return dto;
	}
}
