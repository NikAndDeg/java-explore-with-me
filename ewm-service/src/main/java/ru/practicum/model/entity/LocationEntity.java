package ru.practicum.model.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.List;

@Getter
@Setter
@Entity
@Table(name = "locations")
public class LocationEntity {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "location_id")
	private Integer id;

	@OneToMany(fetch = FetchType.LAZY)
	@JoinColumn(name = "location_id")
	private List<EventEntity> events;

	@Column(name = "lat", nullable = false)
	private float lat;

	@Column(name = "lon", nullable = false)
	private float lon;
}
