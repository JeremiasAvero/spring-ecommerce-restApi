package com.jeremiasAvero.app.products.infraestructure.out;

import com.jeremiasAvero.app.products.domain.ProductEntity;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface JpaProductRepository extends JpaRepository<ProductEntity, Long> {

	 @EntityGraph(attributePaths = {"brand","category","images"})
	  Page<ProductEntity> findAll(Pageable pageable);
}
