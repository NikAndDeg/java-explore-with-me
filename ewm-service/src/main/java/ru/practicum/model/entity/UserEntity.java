package ru.practicum.model.entity;

import lombok.Getter;
import lombok.Setter;

import jakarta.persistence.*;
import java.util.List;

@Getter
@Setter
@Entity
@Table(name = "users")
public class UserEntity {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "user_id")
	private Integer id;

	@OneToMany(fetch = FetchType.LAZY)
	@JoinColumn(name = "initiator_id")
	private List<EventEntity> events;

	@OneToMany(mappedBy = "requester", fetch = FetchType.LAZY)
	private List<ParticipationRequestEntity> requests;

	@Column(name = "user_name", length = 250, nullable = false)
	private String name;

	@Column(name = "user_email", length = 254, nullable = false, unique = true)
	private String email;
}
