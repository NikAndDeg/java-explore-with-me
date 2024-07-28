package ru.practicum.repository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.jdbc.Sql;
import ru.practicum.model.Stat;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@DataJpaTest
@Sql("/hit_repository_test_schema.sql")
class HitRepositoryTest {
	@Autowired
	private HitRepository hitRepository;

	private List<Stat> expected;
	private List<Stat> actual;

	@Test
	void find_stat_test_uris_is_null() {
		expected = List.of(
				createStat("some.app", "/goodbye-world", 2L),
				createStat("some.app", "/hello-world", 2L),
				createStat("another.app", "/hello-world", 1L)
		);

		actual = hitRepository.findStat(
				LocalDateTime.parse("2020-01-01T10:00:00"),
				LocalDateTime.parse("2020-01-01T14:00:00"),
				null
		);

		assertEquals(3, actual.size());
		assertEquals(expected.get(0), actual.get(0));
		assertEquals(expected.get(1), actual.get(1));
		assertEquals(expected.get(2), actual.get(2));
	}

	@Test
	void find_stat_test_distinct() {
		expected = List.of(
				createStat("some.app", "/goodbye-world", 1L),
				createStat("some.app", "/hello-world", 1L)
		);

		actual = hitRepository.findStatDistinct(
				LocalDateTime.parse("2020-01-01T10:00:00"),
				LocalDateTime.parse("2020-01-01T13:00:00"),
				null
		);

		assertEquals(2, actual.size());
		assertEquals(expected.get(0), actual.get(0));
		assertEquals(expected.get(1), actual.get(1));
	}

	@Test
	void find_stat_test_uris_is_not_null() {
		expected = List.of(
				createStat("some.app", "/hello-world", 2L),
				createStat("another.app", "/hello-world", 1L)
		);

		actual = hitRepository.findStat(
				LocalDateTime.parse("2020-01-01T10:00:00"),
				LocalDateTime.parse("2020-01-01T14:00:00"),
				List.of("/hello-world")
		);

		assertEquals(2, actual.size());
		assertEquals(expected.get(0), actual.get(0));
		assertEquals(expected.get(1), actual.get(1));
	}

	private Stat createStat(String app, String uri, Long hits) {
		return new Stat(app, uri, hits);
	}
}