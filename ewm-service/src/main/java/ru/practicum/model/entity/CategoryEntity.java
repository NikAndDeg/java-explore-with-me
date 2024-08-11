package ru.practicum.model.entity;

import lombok.Getter;
import lombok.Setter;

import jakarta.persistence.*;
import java.util.List;

@Getter
@Setter
@Entity
@Table(name = "categories")
public class CategoryEntity {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "category_id")
	private Integer id;

	@OneToMany(mappedBy = "category", fetch = FetchType.LAZY)
	private List<EventEntity> events;

	@Column(name = "category_name", length = 50, nullable = false, unique = true)
	private String name;
}
