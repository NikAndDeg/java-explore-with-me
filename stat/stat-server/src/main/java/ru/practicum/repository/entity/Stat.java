package ru.practicum.repository.entity;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Stat {
	private String app;
	private String uri;
	private Long hits;
}