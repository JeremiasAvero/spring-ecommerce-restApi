package com.jeremiasAvero.app.category.infraestructure.ports.out;

import com.jeremiasAvero.app.category.domain.CategoryEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface JpaCategoryRepository extends JpaRepository<CategoryEntity, Long> {
    boolean existsByName(String name);
}
