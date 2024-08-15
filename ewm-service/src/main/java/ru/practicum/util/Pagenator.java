package ru.practicum.util;

import lombok.experimental.UtilityClass;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;


@UtilityClass
public class Pagenator {
	public static Pageable getPageable(int from, int size) {
		return PageRequest.of(from > 0 ? from / size : 0, size);
	}
}
