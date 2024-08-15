package ru.practicum.repostitory;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.jdbc.Sql;
import ru.practicum.model.entity.CompilationEntity;
import ru.practicum.model.entity.EventEntity;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@Sql("/test_schema.sql")
class CompilationRepositoryTest {
	@Autowired
	private CompilationRepository repository;
	private CompilationEntity compilation;

	@Test
	void without_entity_graph_test() {
		System.out.println("Получение компиляции без EntityGraph.");
		compilation = repository.findById(1).get();
		printCompilation(compilation);
	}

	@Test
	void with_entity_graph_test() {
		System.out.println("Получение компиляции с EntityGraph.");
		compilation = repository.findWithEventsWithCategoryAndInitiatorById(1).get();
		printCompilation(compilation);
	}

	private void printCompilation(CompilationEntity compilation) {
		System.out.println("Compilation title: " + compilation.getTitle());
		List<EventEntity> events = compilation.getEvents();
		System.out.println("Events [");
		events.forEach(event -> {
			System.out.println("Event title: " + event.getTitle());
			System.out.println("Initiator name: " + event.getInitiator().getName());
		});
		System.out.println("]");
	}
}