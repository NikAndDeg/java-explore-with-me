package ru.practicum.service.category;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import ru.practicum.exception.BadRequestException;
import ru.practicum.exception.DataConflictException;
import ru.practicum.exception.DataNotFoundException;
import ru.practicum.model.dto.event.category.CategoryDto;
import ru.practicum.model.dto.event.category.NewCategoryDto;
import ru.practicum.model.entity.CategoryEntity;
import ru.practicum.repostitory.CategoryRepository;
import ru.practicum.util.Pagenator;
import ru.practicum.util.mapper.CategoryMapper;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {
	private final CategoryRepository categoryRepository;

	@Override
	public CategoryDto addCategory(NewCategoryDto newCategoryDto) throws DataConflictException {
		try {
			CategoryEntity savedCategory = categoryRepository.save(CategoryMapper.newCategoryDtoToEntity(newCategoryDto));
			return CategoryMapper.entityToDto(savedCategory);
		} catch (DataIntegrityViolationException e) {
			throw new DataConflictException(e.getMessage());
		}
	}

	@Override
	public CategoryDto deleteCategory(int categoryId) throws DataNotFoundException, DataConflictException {
		CategoryEntity categoryToDelete = categoryRepository.findById(categoryId).orElseThrow(
				() -> new DataNotFoundException("Category with id[" + categoryId + "] doesn't exist.")
		);
		try {
			categoryRepository.deleteById(categoryId);
		} catch (DataIntegrityViolationException e) {
			throw new DataConflictException(e.getMessage());
		}
		return CategoryMapper.entityToDto(categoryToDelete);
	}

	@Override
	public CategoryDto updateCategory(int categoryId, NewCategoryDto newCategoryDto) throws BadRequestException, DataNotFoundException, DataConflictException {
		CategoryEntity categoryToUpdate = categoryRepository.findById(categoryId).orElseThrow(
				() -> new DataNotFoundException("Category with id[" + categoryId + "] doesn't exist.")
		);
		categoryToUpdate.setName(newCategoryDto.getName());
		try {
			CategoryEntity updatedCategory = categoryRepository.save(categoryToUpdate);
			return CategoryMapper.entityToDto(updatedCategory);
		} catch (DataIntegrityViolationException e) {
			throw new DataConflictException(e.getMessage());
		}
	}

	@Override
	public List<CategoryDto> getCategories(int from, int size) {
		return categoryRepository.findAll(Pagenator.getPageable(from, size)).getContent().stream()
				.map(CategoryMapper::entityToDto)
				.collect(Collectors.toList());
	}

	@Override
	public CategoryDto getCategory(int categoryId) throws DataNotFoundException {
		CategoryEntity category = categoryRepository.findById(categoryId).orElseThrow(
				() -> new DataNotFoundException("Category with id[" + categoryId + "] doesn't exist.")
		);
		return CategoryMapper.entityToDto(category);
	}
}
