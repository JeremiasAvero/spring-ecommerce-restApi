package com.jeremiasAvero.app.brand.infraestructure.ports.out;

import com.jeremiasAvero.app.brand.domain.BrandEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface JpaBrandRepository extends JpaRepository<BrandEntity, Long> {
    boolean existsByName(String name);
}
