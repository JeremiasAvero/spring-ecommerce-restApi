package com.jeremiasAvero.app.brand.domain;

import com.jeremiasAvero.app.category.domain.CategoryEntity;

import java.util.List;
import java.util.Optional;

public interface BrandCategory
{
    List<BrandEntity> findAll();
    Optional<BrandEntity> findById(Long id);
    BrandEntity save(BrandEntity brand);
    BrandEntity update(Long id, BrandEntity brand);
    void deleteById(Long id);
}
