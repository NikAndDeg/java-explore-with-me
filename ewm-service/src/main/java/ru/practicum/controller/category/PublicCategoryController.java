package ru.practicum.controller.category;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.model.dto.event.category.CategoryDto;
import ru.practicum.service.category.CategoryService;

import jakarta.validation.constraints.Min;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Validated
@RestController
@RequestMapping(path = "/categories")
public class PublicCategoryController {
	private final CategoryService categoryService;

	//GET /categories?from=0&size=10
	@GetMapping
	public List<CategoryDto> getCategories(@RequestParam(defaultValue = "0") @Min(0) int from,
										   @RequestParam(defaultValue = "10") @Min(1) int size) {
		log.info("Request to get categories with param from[{}], size[{}].", from, size);
		List<CategoryDto> categoriesDto = categoryService.getCategories(from, size);
		log.info("Categories received.");
		return categoriesDto;
	}

	//GET /categories/{catId}
	@GetMapping("/{catId}")
	public CategoryDto getCategory(@PathVariable @Min(1) int catId) {
		log.info("Request to get category by id[{}].", catId);
		CategoryDto categoryDto = categoryService.getCategory(catId);
		log.info("Category[{}] found.", categoryDto);
		return categoryDto;
	}
}
