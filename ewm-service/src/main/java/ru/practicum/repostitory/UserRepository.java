package ru.practicum.repostitory;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.model.entity.UserEntity;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<UserEntity, Integer> {

	List<UserEntity> findAllByIdIn(List<Integer> ids, Pageable pageable);

	@EntityGraph(attributePaths = {"requests", "requests.event"})
	Optional<UserEntity> findWithRequestsAndEventsById(int userId);

	@EntityGraph(attributePaths = {"requests"})
	Optional<UserEntity> findWithRequestsById(int userId);
}
