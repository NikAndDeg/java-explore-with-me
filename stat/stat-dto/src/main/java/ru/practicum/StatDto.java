package ru.practicum;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class StatDto {
	private String app;
	private String uri;
	private Long hits;
}
