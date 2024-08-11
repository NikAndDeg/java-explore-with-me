package ru.practicum.controller.compilation;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.model.dto.event.compilation.CompilationDto;
import ru.practicum.model.dto.event.compilation.NewCompilationDto;
import ru.practicum.model.dto.request.UpdateCompilationRequest;
import ru.practicum.service.compilation.CompilationService;

import javax.validation.Valid;
import javax.validation.constraints.Min;

@Slf4j
@RequiredArgsConstructor
@Validated
@RestController
@RequestMapping(path = "/admin/compilations")
public class AdminCompilationController {
	private final CompilationService compilationService;

	//POST /admin/compilations
	@ResponseStatus(HttpStatus.CREATED)
	@PostMapping
	public CompilationDto addCompilation(@RequestBody @Valid NewCompilationDto newCompilationDto) {
		//подборка может не содержать событий
		log.info("Request to add new Compilation[{}].", newCompilationDto);
		CompilationDto savedCompilationDto = compilationService.addCompilation(newCompilationDto);
		log.info("Compilation[{}] saved.", savedCompilationDto);
		return savedCompilationDto;
	}

	//DELETE /admin/compilations/{compId}
	@ResponseStatus(HttpStatus.NO_CONTENT)
	@DeleteMapping("/{compId}")
	public CompilationDto deleteCompilation(@PathVariable @Min(1) int compId) {
		log.info("Request to delete compilation by id[{}].", compId);
		CompilationDto deletedCompilationDto = compilationService.deleteCompilation(compId);
		log.info("Compilation [{}] deleted.", deletedCompilationDto);
		return deletedCompilationDto;
	}

	//PATCH /admin/compilations/{compId}

	@PatchMapping("/{compId}")
	public CompilationDto updateCompilation(@PathVariable @Min(1) int compId,
											@RequestBody @Valid UpdateCompilationRequest request) {
		log.info("Request to update compilation[{}] by id[{}].", request, compId);
		CompilationDto updatedCompilationDto = compilationService.updateCompilation(compId, request);
		log.info("Compilation[{}] updated.", updatedCompilationDto);
		return updatedCompilationDto;
	}
}
