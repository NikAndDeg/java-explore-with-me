package ru.practicum.model.entity;

import lombok.Getter;
import lombok.Setter;
import ru.practicum.model.event.participation.ParticipationStatus;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@Table(name = "participation_requests")
public class ParticipationRequestEntity {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "request_id")
	private Integer id;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "user_id", nullable = false)
	private UserEntity requester;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "event_id", nullable = false)
	private EventEntity event;

	@Column(name = "status_id", nullable = false)
	private ParticipationStatus status;

	@Column(name = "created_on", nullable = false)
	private LocalDateTime createdOn;
}
