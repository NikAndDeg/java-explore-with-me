package ru.practicum.service.compilation;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.exception.DataConflictException;
import ru.practicum.exception.DataNotFoundException;
import ru.practicum.model.dto.event.compilation.CompilationDto;
import ru.practicum.model.dto.event.compilation.NewCompilationDto;
import ru.practicum.model.dto.request.UpdateCompilationRequest;
import ru.practicum.model.entity.CompilationEntity;
import ru.practicum.model.entity.EventEntity;
import ru.practicum.repostitory.CompilationRepository;
import ru.practicum.repostitory.EventRepository;
import ru.practicum.util.Pagenator;
import ru.practicum.util.mapper.CompilationMapper;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class CompilationServiceImpl implements CompilationService {
	private final CompilationRepository compilationRepository;
	private final EventRepository eventRepository;

	@Override
	@Transactional
	public CompilationDto addCompilation(NewCompilationDto newCompilationDto) throws DataConflictException {
		CompilationEntity compilation = CompilationMapper.newDtoToEntity(newCompilationDto);

		List<Integer> eventsId = newCompilationDto.getEvents();

		if (eventsId != null && !eventsId.isEmpty()) {
			List<EventEntity> events = eventRepository.findWithCategoryInitiatorAllByIdIn(eventsId);

			compilation.setEvents(events);

			return CompilationMapper.entityToDto(compilationRepository.save(compilation), events);
		} else {
			return CompilationMapper.entityToDtoWithoutEvents(compilationRepository.save(compilation));
		}
	}

	@Override
	@Transactional
	public CompilationDto deleteCompilation(int compilationId) throws DataNotFoundException {
		CompilationEntity compilationToDelete = compilationRepository.findById(compilationId).orElseThrow(
				() -> new DataNotFoundException("Compilation with id[" + compilationId + "] not found.")
		);

		compilationRepository.delete(compilationToDelete);

		return CompilationMapper.entityToDtoWithoutEvents(compilationToDelete);
	}

	@Override
	@Transactional
	public CompilationDto updateCompilation(int compilationId, UpdateCompilationRequest request) throws DataNotFoundException {
		CompilationEntity compilationToUpdate = compilationRepository.findWithEventsWithCategoryAndInitiatorById(compilationId).orElseThrow(
				() -> new DataNotFoundException("Compilation with id[" + compilationId + "] not found.")
		);

		setNewInfoToCompilation(compilationToUpdate, request);

		List<EventEntity> events = compilationToUpdate.getEvents();

		CompilationEntity updatedCompilation = compilationRepository.save(compilationToUpdate);

		return CompilationMapper.entityToDto(updatedCompilation, events);
	}

	@Override
	@Transactional(readOnly = true)
	public List<CompilationDto> getCompilations(Boolean pinned, int from, int size) {
		List<CompilationEntity> compilations;

		if (pinned != null) {
			compilations = compilationRepository.findWithEventsWithCategoryAndInitiatorAllByPinnedIn(
					List.of(pinned),
					Pagenator.getPageable(from, size));
		} else {
			compilations = compilationRepository.findWithEventsWithCategoryAndInitiatorAllByPinnedIn(
					List.of(true, false),
					Pagenator.getPageable(from, size));
		}

		return compilations.stream()
				.map(compilation -> CompilationMapper.entityToDto(compilation, compilation.getEvents()))
				.collect(Collectors.toList());
	}

	@Override
	@Transactional(readOnly = true)
	public CompilationDto getCompilation(int compilationId) throws DataNotFoundException {
		CompilationEntity compilation = compilationRepository.findWithEventsWithCategoryAndInitiatorById(compilationId).orElseThrow(
				() -> new DataNotFoundException("Compilation with id[" + compilationId + "] not found.")
		);

		return CompilationMapper.entityToDto(compilation, compilation.getEvents());
	}

	private void setNewInfoToCompilation(CompilationEntity compilationToUpdate, UpdateCompilationRequest request) {
		List<Integer> eventsId = request.getEvents();
		if (eventsId != null && !eventsId.isEmpty()) {
			List<EventEntity> events = eventRepository.findWithCategoryInitiatorAllByIdIn(eventsId);
			if (events.size() != eventsId.size())
				throw new DataNotFoundException("One of events doesn't exist.");
			compilationToUpdate.setEvents(events);
		}
		if (request.getPinned() != null)
			compilationToUpdate.setPinned(request.getPinned());
		if (request.getTitle() != null)
			compilationToUpdate.setTitle(request.getTitle());
	}
}
