package ru.practicum.model.entity;

import lombok.Getter;
import lombok.Setter;
import ru.practicum.model.event.EventState;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@Entity
@Table(name = "events")
public class EventEntity {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "event_id")
	private Integer id;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "category_id")
	private CategoryEntity category;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "initiator_id")
	private UserEntity initiator;

	@ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	@JoinColumn(name = "location_id")
	private LocationEntity location;

	@OneToMany(mappedBy = "event", fetch = FetchType.LAZY)
	private List<ParticipationRequestEntity> requests;

	@OneToMany(mappedBy = "event", fetch = FetchType.LAZY)
	private List<CommentEntity> comments;

	@Enumerated(EnumType.ORDINAL)
	@Column(name = "event_state_id")
	private EventState state;

	@Column(name = "title", length = 120, nullable = false)
	private String title;

	@Column(name = "annotation", length = 2000, nullable = false)
	private String annotation;

	@Column(name = "description", length = 7000, nullable = false)
	private String description;

	@Column(name = "participant_limit", nullable = false)
	private Integer participantLimit;

	@Column(name = "confirmed_requests", nullable = false)
	private Integer confirmedRequests;

	@Column(name = "created_on", nullable = false)
	private LocalDateTime createdOn;

	@Column(name = "event_date", nullable = false)
	private LocalDateTime eventDate;

	@Column(name = "published_on")
	private LocalDateTime publishedOn;

	@Column(name = "paid", nullable = false)
	private Boolean paid;

	@Column(name = "request_moderation", nullable = false)
	private Boolean requestModeration;
}
