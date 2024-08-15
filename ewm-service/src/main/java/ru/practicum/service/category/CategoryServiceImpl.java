package ru.practicum.service.category;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
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
	@Transactional
	public CategoryDto addCategory(NewCategoryDto newCategoryDto) throws DataConflictException {
		CategoryEntity savedCategory = categoryRepository.save(CategoryMapper.newCategoryDtoToEntity(newCategoryDto));
		return CategoryMapper.entityToDto(savedCategory);
	}

	@Override
	@Transactional
	public CategoryDto deleteCategory(int categoryId) throws DataNotFoundException, DataConflictException {
		CategoryEntity categoryToDelete = categoryRepository.findById(categoryId).orElseThrow(
				() -> new DataNotFoundException("Category with id[" + categoryId + "] doesn't exist.")
		);
		categoryRepository.deleteById(categoryId);
		return CategoryMapper.entityToDto(categoryToDelete);
	}

	@Override
	@Transactional
	public CategoryDto updateCategory(int categoryId, NewCategoryDto newCategoryDto) throws BadRequestException,
			DataNotFoundException, DataConflictException {
		CategoryEntity categoryToUpdate = categoryRepository.findById(categoryId).orElseThrow(
				() -> new DataNotFoundException("Category with id[" + categoryId + "] doesn't exist.")
		);
		categoryToUpdate.setName(newCategoryDto.getName());
		CategoryEntity updatedCategory = categoryRepository.save(categoryToUpdate);
		return CategoryMapper.entityToDto(updatedCategory);
	}

	@Override
	@Transactional(readOnly = true)
	public List<CategoryDto> getCategories(int from, int size) {
		Page<CategoryEntity> page = categoryRepository.findAll(Pagenator.getPageable(from, size));
		return page.getContent()
				.stream()
				.map(CategoryMapper::entityToDto)
				.collect(Collectors.toList());
	}

	@Override
	@Transactional(readOnly = true)
	public CategoryDto getCategory(int categoryId) throws DataNotFoundException {
		CategoryEntity category = categoryRepository.findById(categoryId).orElseThrow(
				() -> new DataNotFoundException("Category with id[" + categoryId + "] doesn't exist.")
		);
		return CategoryMapper.entityToDto(category);
	}
}
