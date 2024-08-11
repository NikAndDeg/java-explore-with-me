package ru.practicum.controller.category;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.model.dto.event.category.CategoryDto;
import ru.practicum.model.dto.event.category.NewCategoryDto;
import ru.practicum.service.category.CategoryService;

import javax.validation.Valid;
import javax.validation.constraints.Min;

@Slf4j
@RequiredArgsConstructor
@Validated
@RestController
@RequestMapping(path = "/admin/categories")
public class AdminCategoryController {
	private final CategoryService categoryService;

	//POST /admin/categories
	@ResponseStatus(HttpStatus.CREATED)
	@PostMapping
	public CategoryDto addCategory(@RequestBody @Valid NewCategoryDto newCategoryDto) {
		log.info("Request to add category[{}].", newCategoryDto);
		CategoryDto savedCategoryDto = categoryService.addCategory(newCategoryDto);
		log.info("Category[{}] saved.", savedCategoryDto);
		return savedCategoryDto;
	}

	//DELETE /admin/categories/{catId}
	@ResponseStatus(HttpStatus.NO_CONTENT)
	@DeleteMapping("/{catId}")
	public CategoryDto deleteCategory(@PathVariable @Min(1) int catId) {
		log.info("Request to delete category by id[{}].", catId);
		CategoryDto deletedCategoryDto = categoryService.deleteCategory(catId);
		log.info("Category[{}] deleted.", deletedCategoryDto);
		return deletedCategoryDto;
	}

	//PATCH /admin/categories/{catId}
	@PatchMapping("/{catId}")
	public CategoryDto updateCategory(@PathVariable @Min(1) int catId,
									  @RequestBody @Valid NewCategoryDto newCategoryDto) {
		log.info("Request to update category[{}] by id[{}].", newCategoryDto, catId);
		CategoryDto updatedCategoryDto = categoryService.updateCategory(catId, newCategoryDto);
		log.info("Category[{}] updated.", updatedCategoryDto);
		return updatedCategoryDto;
	}
}
