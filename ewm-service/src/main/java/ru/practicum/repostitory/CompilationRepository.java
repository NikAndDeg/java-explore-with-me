package ru.practicum.repostitory;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.model.entity.CompilationEntity;

import java.util.List;
import java.util.Optional;

public interface CompilationRepository extends JpaRepository<CompilationEntity, Integer> {
	@EntityGraph(attributePaths = {"events", "events.category", "events.initiator"})
	Optional<CompilationEntity> findWithEventsWithCategoryAndInitiatorById(int compilationId);

	@EntityGraph(attributePaths = {"events", "events.category", "events.initiator"})
	List<CompilationEntity> findWithEventsWithCategoryAndInitiatorAllByPinnedIn(List<Boolean> pinned, Pageable pageable);
}
