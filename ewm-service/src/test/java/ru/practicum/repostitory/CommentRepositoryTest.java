package ru.practicum.repostitory;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.jdbc.Sql;
import ru.practicum.model.entity.CommentEntity;
import ru.practicum.util.Pagenator;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@Sql("/test_schema.sql")
class CommentRepositoryTest {
	@Autowired
	private CommentRepository repository;
	private List<CommentEntity> comments;
	private final Pageable pageable = Pagenator.getPageable(0, 10);

	@Test
	void event_id_search_test() {
		comments = repository.findWithCommenterBySearchParam(1, null, null, null, pageable);
		assertEquals(3, comments.size());
		assertEquals(1, comments.get(0).getId());
		assertEquals(2, comments.get(1).getId());
		assertEquals(3, comments.get(2).getId());
	}

	@Test
	void text_search_test() {
		comments = repository.findWithCommenterBySearchParam(1, "qwerty", null, null, pageable);
		assertEquals(1, comments.size());
		assertEquals(1, comments.getFirst().getId());

		comments = repository.findWithCommenterBySearchParam(1, "eeeeeee", null, null, pageable);
		assertEquals(1, comments.size());
		assertEquals(2, comments.getFirst().getId());

		comments = repository.findWithCommenterBySearchParam(1, "qwe", null, null, pageable);
		assertEquals(2, comments.size());
		assertEquals(1, comments.get(0).getId());
		assertEquals(2, comments.get(1).getId());

		comments = repository.findWithCommenterBySearchParam(1, "text", null, null, pageable);
		assertEquals(3, comments.size());
		assertEquals(1, comments.get(0).getId());
		assertEquals(2, comments.get(1).getId());
		assertEquals(3, comments.get(2).getId());
	}

	@Test
	void time_search_test() {
		comments = repository.findWithCommenterBySearchParam(1, null,
				LocalDateTime.of(2100, 1, 1, 12, 0), null, pageable);
		assertEquals(1, comments.size());
		assertEquals(3, comments.getFirst().getId());

		comments = repository.findWithCommenterBySearchParam(1, null,
				null, LocalDateTime.of(2000, 1, 1, 12, 0), pageable);
		assertEquals(1, comments.size());
		assertEquals(1, comments.getFirst().getId());

		comments = repository.findWithCommenterBySearchParam(2, null,
				LocalDateTime.of(2000, 1, 1, 12, 0),
				LocalDateTime.of(2021, 1, 1, 12, 0), pageable);
		assertEquals(2, comments.size());
		assertEquals(4, comments.get(0).getId());
		assertEquals(5, comments.get(1).getId());
	}
}