package com.jeremiasAvero.app.products.domain;

import java.util.List;
import java.util.Optional;

public interface ProductRepository {
    List<ProductEntity> findAll();
    Optional<ProductEntity> findById(Long id);
    ProductEntity save(ProductEntity product);
    ProductEntity update(Long id, ProductEntity product);
    void deleteById(Long id);
}
