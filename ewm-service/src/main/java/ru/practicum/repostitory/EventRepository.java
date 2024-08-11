package ru.practicum.repostitory;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.model.entity.EventEntity;
import ru.practicum.model.event.EventState;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface EventRepository extends JpaRepository<EventEntity, Integer> {

	@EntityGraph(attributePaths = {"category", "initiator", "location"})
	Optional<EventEntity> findWithCategoryInitiatorLocationByIdAndStateIn(int eventId, List<EventState> states);

	@EntityGraph(attributePaths = {"category", "initiator", "location"})
	Optional<EventEntity> findWithCategoryInitiatorLocationById(int eventId);

	@EntityGraph(attributePaths = {"category"})
	List<EventEntity> findWithCategoryAllByInitiatorId(int initiatorId, Pageable pageable);

	@EntityGraph(attributePaths = {"category", "initiator"})
	List<EventEntity> findWithCategoryInitiatorAllByIdIn(List<Integer> eventsId);

	@EntityGraph(attributePaths = {"initiator"})
	Optional<EventEntity> findWithInitiatorById(int eventId);

	@EntityGraph(attributePaths = {"initiator", "requests", "requests.requester"})
	Optional<EventEntity> findWithInitiatorRequestsWithRequesterById(int eventId);

	@Query("SELECT e FROM EventEntity e " +
			"JOIN e.initiator u " +
			"JOIN e.category c " +
			"WHERE (:usersId IS NULL OR u.id IN :usersId) " +
			"AND (:states IS NULL OR e.state IN :states) " +
			"AND (:categoriesId IS NULL or c.id IN :categoriesId) " +
			"AND ( (cast(:start as timestamp) IS NULL AND e.eventDate >= NOW()) OR e.eventDate >= :start) " +
			"AND (cast(:end as timestamp) IS NULL OR e.eventDate <= :end) ")
	@EntityGraph(attributePaths = {"category", "initiator", "location"})
	List<EventEntity> findWithCategoryInitiatorLocationBySearchParam(List<Integer> usersId, List<EventState> states,
																	 List<Integer> categoriesId, LocalDateTime start,
																	 LocalDateTime end, Pageable pageable);

	@Query("SELECT e FROM EventEntity e " +
			"JOIN e.category c " +
			"WHERE (:eventState IS NULL OR e.state = :eventState) " +
			"AND (:text IS NULL OR ( (lower(e.annotation) like lower(concat('%', :text,'%'))) " +
				"OR (lower(e.description) like lower(concat('%', :text,'%'))) ) ) " +
			"AND ( (cast(:start as timestamp) IS NULL AND e.eventDate >= NOW()) OR e.eventDate >= :start) " +
			"AND (cast(:end as timestamp) IS NULL OR e.eventDate <= :end) " +
			"AND (:categoriesId IS NULL or c.id IN :categoriesId) " +
			"AND (:paid IS NULL OR e.paid = :paid) " +
			"AND (:onlyAvailable IS NULL OR :onlyAvailable = false OR (e.participantLimit = 0 OR e.participantLimit < e.confirmedRequests)) ")
	@EntityGraph(attributePaths = {"category", "initiator"})
	List<EventEntity> findWithCategoryAndInitiatorBySearchParam(EventState eventState, String text, LocalDateTime start,
																LocalDateTime end, List<Integer> categoriesId,
																Boolean paid, Boolean onlyAvailable, Pageable pageable);
}
