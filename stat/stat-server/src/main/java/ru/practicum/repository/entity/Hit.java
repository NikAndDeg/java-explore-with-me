package ru.practicum.repository.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;

@Getter
@Setter
@ToString
@Entity
@Table(name = "hits", schema = "public")
public class Hit {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "hit_id")
	private Integer id;

	@Column(name = "app", length = 50, nullable = false)
	private String app;

	@Column(name = "uri", length = 50, nullable = false)
	private String uri;

	@Column(name = "ip", length = 50, nullable = false)
	private String ip;

	@Column(name = "hit_timestamp", nullable = false)
	private LocalDateTime timestamp;
}
