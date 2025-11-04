package com.jeremiasAvero.app.products.infraestructure.out;

import com.jeremiasAvero.app.products.domain.ProductEntity;
import com.jeremiasAvero.app.products.domain.ProductRepository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

@Component
public class ProductRepositoryImpl implements ProductRepository {
    private final JpaProductRepository repository;

    public ProductRepositoryImpl(JpaProductRepository repository){
        this.repository = repository;
    }

    @Override
    public List<ProductEntity> findAll() {
        return repository.findAll();
    }

    @Override
    public Optional<ProductEntity> findById(Long id) {
        return repository.findById(id);
    }

    @Override
    public ProductEntity save(ProductEntity product) {
        return repository.save(product);
    }

    @Override
    public ProductEntity update(Long id, ProductEntity product) {
        product.setId(id);
        return repository.save(product);
    }

    @Override
    public void deleteById(Long id) {
        repository.deleteById(id);
    }

	@Override
	public Page<ProductEntity> findAllPageable(Pageable pageable) {
		return repository.findAll(pageable);
	}
}
