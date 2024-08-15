package ru.practicum.service.category;

import ru.practicum.exception.DataConflictException;
import ru.practicum.exception.DataNotFoundException;
import ru.practicum.model.dto.event.category.CategoryDto;
import ru.practicum.model.dto.event.category.NewCategoryDto;

import java.util.List;

public interface CategoryService {
	CategoryDto addCategory(NewCategoryDto newCategoryDto) throws DataConflictException;

	CategoryDto deleteCategory(int categoryId) throws DataNotFoundException,
			DataConflictException;

	CategoryDto updateCategory(int categoryId, NewCategoryDto newCategoryDto) throws DataNotFoundException,
			DataConflictException;

	List<CategoryDto> getCategories(int from, int size);

	CategoryDto getCategory(int categoryId) throws DataNotFoundException;
}
