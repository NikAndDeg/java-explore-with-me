package ru.practicum.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.model.Hit;
import ru.practicum.model.Stat;

import java.time.LocalDateTime;
import java.util.List;

public interface HitRepository extends JpaRepository<Hit, Integer> {
	@Query("SELECT new ru.practicum.model.Stat(h.app, h.uri, COUNT(h.ip)) " +
			"FROM Hit AS h " +
			"WHERE h.timestamp BETWEEN :start AND :end " +
			"AND(:uris IS NULL OR h.uri IN :uris) " +
			"GROUP BY h.app, h.uri " +
			"ORDER BY COUNT(h.ip) DESC")
	List<Stat> findStat(LocalDateTime start, LocalDateTime end, List<String> uris);

	@Query("SELECT new ru.practicum.model.Stat(h.app, h.uri, COUNT(DISTINCT h.ip)) " +
			"FROM Hit AS h " +
			"WHERE h.timestamp BETWEEN :start AND :end " +
			"AND(:uris IS NULL OR h.uri IN :uris) " +
			"GROUP BY h.app, h.uri " +
			"ORDER BY COUNT(DISTINCT h.ip) DESC")
	List<Stat> findStatDistinct(LocalDateTime start, LocalDateTime end, List<String> uris);
}