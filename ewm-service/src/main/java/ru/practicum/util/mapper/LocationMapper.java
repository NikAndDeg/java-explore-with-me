package ru.practicum.util.mapper;

import lombok.experimental.UtilityClass;
import ru.practicum.model.entity.LocationEntity;
import ru.practicum.model.event.location.Location;

@UtilityClass
public class LocationMapper {
	public static Location entityToDto(LocationEntity entity) {
		Location location = new Location();
		location.setLat(entity.getLat());
		location.setLon(entity.getLon());
		return location;
	}

	public static LocationEntity dtoToEntity(Location location) {
		LocationEntity entity = new LocationEntity();
		entity.setLat(location.getLat());
		entity.setLon(location.getLon());
		return entity;
	}
}
