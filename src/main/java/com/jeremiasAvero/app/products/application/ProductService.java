package com.jeremiasAvero.app.products.application;

import com.jeremiasAvero.app.products.application.exception.ProductNotFoundException;
import com.jeremiasAvero.app.products.domain.ProductEntity;
import com.jeremiasAvero.app.products.domain.ProductRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class ProductService {

    private final ProductRepository productRepository;

    public ProductService(ProductRepository productRepository){
        this.productRepository = productRepository;
    }

    @Transactional(readOnly = true)
    public List<ProductEntity> findAll(){
        return productRepository.findAll();
    }

    @Transactional(readOnly = true)
    public ProductEntity findById(Long id) throws Exception {
        return productRepository.findById(id).orElseThrow(() -> new Exception(""));
    }

    public ProductEntity save(ProductEntity product){
        return productRepository.save(product);
    }

    @Transactional
    public ProductEntity update(Long id, ProductEntity product) {
        ProductEntity existing = productRepository.findById(id)
                .orElseThrow(() -> new ProductNotFoundException(id));


        existing.setName(product.getName());
        existing.setDescription(product.getDescription());
        existing.setStock(product.getStock());
        existing.setPrice(product.getPrice());
        existing.setCategory(product.getCategory());
        existing.setBrand(product.getBrand());

        return productRepository.update(id, existing);
    }

    @Transactional
    public void deleteById(Long id) {
        if (productRepository.findById(id).isEmpty()) {
            throw new ProductNotFoundException(id);
        }
        productRepository.deleteById(id);
    }


}
