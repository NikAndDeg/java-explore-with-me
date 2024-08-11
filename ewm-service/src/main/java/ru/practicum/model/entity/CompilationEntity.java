package ru.practicum.model.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.List;

@Getter
@Setter
@Entity
@Table(name = "compilations")
public class CompilationEntity {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "compilation_id")
	private Integer id;

	@ManyToMany(fetch = FetchType.LAZY)
	@JoinTable(
			name = "compilations_events",
			joinColumns = @JoinColumn(name = "compilation_id"),
			inverseJoinColumns = @JoinColumn(name = "event_id")
	)
	private List<EventEntity> events;

	@Column(name = "compilation_title", length = 50, nullable = false, unique = true)
	private String title;

	@Column(name = "pinned", nullable = false)
	private Boolean pinned;
}
