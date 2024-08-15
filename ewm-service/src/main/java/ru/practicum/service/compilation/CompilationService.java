package ru.practicum.service.compilation;

import ru.practicum.exception.DataConflictException;
import ru.practicum.exception.DataNotFoundException;
import ru.practicum.model.dto.event.compilation.CompilationDto;
import ru.practicum.model.dto.event.compilation.NewCompilationDto;
import ru.practicum.model.dto.request.UpdateCompilationRequest;

import java.util.List;

public interface CompilationService {
	CompilationDto addCompilation(NewCompilationDto newCompilationDto) throws DataConflictException;

	CompilationDto deleteCompilation(int compilationId) throws DataNotFoundException;

	CompilationDto updateCompilation(int compilationId, UpdateCompilationRequest request) throws DataNotFoundException;

	List<CompilationDto> getCompilations(Boolean pinned, int from, int size);

	CompilationDto getCompilation(int compilationId) throws DataNotFoundException;
}
