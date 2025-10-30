package com.jeremiasAvero.app.brand.domain;

import java.util.List;
import java.util.Optional;

public interface BrandRepository
{
    List<BrandEntity> findAll();
    Optional<BrandEntity> findById(Long id);
    BrandEntity save(BrandEntity brand);
    boolean existsByName(String name);
    BrandEntity update(Long id, BrandEntity brand);
    void deleteById(Long id);
    Optional<BrandEntity> findByName(String name);
}
