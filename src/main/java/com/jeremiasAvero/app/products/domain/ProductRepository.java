package com.jeremiasAvero.app.products.domain;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ProductRepository {
    List<ProductEntity> findAll();
    Page<ProductEntity> findAllPageable(Pageable pageable);
    Optional<ProductEntity> findById(Long id);
    ProductEntity save(ProductEntity product);
    ProductEntity update(Long id, ProductEntity product);
    void deleteById(Long id);
    List<ProductEntity> findAllById(Set<Long> productIds);
}
