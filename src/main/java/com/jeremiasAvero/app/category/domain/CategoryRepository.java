package com.jeremiasAvero.app.category.domain;

import java.util.List;
import java.util.Optional;

public interface CategoryRepository {
    List<CategoryEntity> findAll();
    Optional<CategoryEntity> findById(Long id);
    boolean existsByName(String name);
    CategoryEntity save(CategoryEntity category);
    CategoryEntity update(Long id, CategoryEntity category);
    void deleteById(Long id);

}
