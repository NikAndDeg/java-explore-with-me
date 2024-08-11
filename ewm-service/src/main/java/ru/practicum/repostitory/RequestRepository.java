package ru.practicum.repostitory;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.model.entity.ParticipationRequestEntity;

import java.util.Optional;

public interface RequestRepository extends JpaRepository<ParticipationRequestEntity, Integer> {
	@EntityGraph(attributePaths = {"requester", "event"})
	Optional<ParticipationRequestEntity> findWithRequesterEventById(int requestId);
}
