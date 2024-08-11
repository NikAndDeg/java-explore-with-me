package ru.practicum.repostitory;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.model.entity.CategoryEntity;

public interface CategoryRepository extends JpaRepository<CategoryEntity, Integer> {
}
