package ru.practicum.repostitory;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.model.entity.CommentEntity;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface CommentRepository extends JpaRepository<CommentEntity,Integer> {
	@EntityGraph(attributePaths = {"commenter"})
	Optional<CommentEntity> findWithCommenterById(int commentId);

	@Query("SELECT c FROM CommentEntity c " +
			"JOIN c.event e " +
			"WHERE (:eventId IS NULL OR e.id = :eventId) " +
			"AND (lower(:text) IS NULL OR lower(c.text) like lower(concat('%', :text,'%'))) " +
			"AND (cast(:start as timestamp) IS NULL OR c.createdOn >= :start) " +
			"AND (cast(:end as timestamp) IS NULL OR c.createdOn <= :end) ")
	@EntityGraph(attributePaths = {"commenter"})
	List<CommentEntity> findWithCommenterBySearchParam(Integer eventId, String text, LocalDateTime start, LocalDateTime end,
													   Pageable pageable);
}
