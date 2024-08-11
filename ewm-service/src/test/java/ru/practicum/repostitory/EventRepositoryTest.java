package ru.practicum.repostitory;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.jdbc.Sql;
import ru.practicum.model.entity.EventEntity;
import ru.practicum.util.Pagenator;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static ru.practicum.model.event.EventState.*;

@DataJpaTest
@Sql("/test_schema.sql")
class EventRepositoryTest {
	@Autowired
	private EventRepository repository;
	private List<EventEntity> events;
	private EventEntity event;
	private final Pageable pageable = Pagenator.getPageable(0, 10);

	@Test
	void text_search_test() {
		events = repository.findWithCategoryAndInitiatorBySearchParam(PUBLISHED, null, null, null,
				null, null, null, pageable);
		assertFalse(events.isEmpty());
		assertEquals(events.size(), 2);
		assertEquals(events.get(0).getId(), 1);
		assertEquals(events.get(1).getId(), 4);

		events = repository.findWithCategoryAndInitiatorBySearchParam(PUBLISHED, " JJJ", null, null,
				null, null, null, pageable);
		assertTrue(events.isEmpty());

		events = repository.findWithCategoryAndInitiatorBySearchParam(PUBLISHED, "aNnOtation", null, null,
				null, null, null, pageable);
		assertFalse(events.isEmpty());
		assertEquals(events.size(), 2);
		assertEquals(events.get(0).getId(), 1);
		assertEquals(events.get(1).getId(), 4);
	}

	@Test
	void time_search_test() {
		events = repository.findWithCategoryAndInitiatorBySearchParam(PUBLISHED, null, null, null,
				null, null, null, pageable);
		assertFalse(events.isEmpty());
		assertEquals(events.size(), 2);
		assertEquals(events.get(0).getId(), 1);
		assertEquals(events.get(1).getId(), 4);

		events = repository.findWithCategoryAndInitiatorBySearchParam(PUBLISHED, null,
				LocalDateTime.now().minusYears(50), null, null, null, null, pageable);
		assertFalse(events.isEmpty());
		assertEquals(events.size(), 4);
		assertEquals(events.get(0).getId(), 1);
		assertEquals(events.get(1).getId(), 2);
		assertEquals(events.get(2).getId(), 3);
		assertEquals(events.get(3).getId(), 4);

		events = repository.findWithCategoryAndInitiatorBySearchParam(PUBLISHED, null, LocalDateTime.now().minusYears(50),
				LocalDateTime.now().minusYears(10), null, null, null, pageable);
		assertFalse(events.isEmpty());
		assertEquals(events.size(), 2);
		assertEquals(events.get(0).getId(), 2);
		assertEquals(events.get(1).getId(), 3);
	}

	@Test
	void categoriesId_search_test() {
		events = repository.findWithCategoryAndInitiatorBySearchParam(PUBLISHED, null, null, null,
				List.of(2), null, null, pageable);
		assertFalse(events.isEmpty());
		assertEquals(events.size(), 1);
		assertEquals(events.get(0).getId(), 4);
	}

	@Test
	void available_search_test() {
		events = repository.findWithCategoryAndInitiatorBySearchParam(PUBLISHED, null, null, null,
				null, null, true, pageable);
		assertFalse(events.isEmpty());
		assertEquals(events.size(), 2);
		assertEquals(events.get(0).getId(), 1);
		assertEquals(events.get(1).getId(), 4);

		EventEntity event = events.get(0);
		event.setConfirmedRequests(event.getParticipantLimit() + 1);
		repository.save(event);

		events = repository.findWithCategoryAndInitiatorBySearchParam(PUBLISHED, null, null, null,
				null, null, true, pageable);
		assertFalse(events.isEmpty());
		assertEquals(events.size(), 1);
		assertEquals(events.get(0).getId(), 4);
	}

	@Test
	void paid_search_test() {
		events = repository.findWithCategoryAndInitiatorBySearchParam(PUBLISHED, null, null, null,
				null, true, null, pageable);
		assertFalse(events.isEmpty());
		assertEquals(events.size(), 1);
		assertEquals(events.get(0).getId(), 1);

		events = repository.findWithCategoryAndInitiatorBySearchParam(PUBLISHED, null, null, null,
				null, false, null, pageable);
		assertFalse(events.isEmpty());
		assertEquals(events.size(), 1);
		assertEquals(events.get(0).getId(), 4);
	}
}