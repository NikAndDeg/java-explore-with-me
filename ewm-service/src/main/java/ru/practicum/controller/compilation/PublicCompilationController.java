package ru.practicum.controller.compilation;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.model.dto.event.compilation.CompilationDto;
import ru.practicum.service.compilation.CompilationService;

import jakarta.validation.constraints.Min;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Validated
@RestController
@RequestMapping(path = "/compilations")
public class PublicCompilationController {
	private final CompilationService compilationService;

	//GET /compilations
	@GetMapping
	public List<CompilationDto> getCompilations(@RequestParam(required = false) boolean pinned,
												@RequestParam(defaultValue = "0") @Min(0) int from,
												@RequestParam(defaultValue = "10") @Min(1) int size) {
		log.info("Request to get compilations with param pinned[{}], from[{}], size[{}].", pinned, from, size);
		List<CompilationDto> compilationsDto = compilationService.getCompilations(pinned, from, size);
		log.info("Compilations received.");
		return compilationsDto;
	}

	//GET /compilations/{compId}
	@GetMapping("{compId}")
	public CompilationDto getCompilation(@PathVariable @Min(1) int compId) {
		//В случае, если подборки с заданным id не найдено, возвращает статус код 404
		log.info("Request to get compilation by id[{}].", compId);
		CompilationDto compilationDto = compilationService.getCompilation(compId);
		log.info("Compilation[{}] found.", compilationDto);
		return compilationDto;
	}
}
