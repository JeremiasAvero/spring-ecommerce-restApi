package com.jeremiasAvero.app.cart.domain;

import java.util.Optional;
import java.util.UUID;

import com.jeremiasAvero.app.products.domain.ProductEntity;

public interface CartRepository {

	Optional<CartEntity> findByUserIdAndActiveTrue(Long userId);

	Optional<CartEntity> findById(UUID cartId);

	CartEntity save(CartEntity cart);

	CartEntity findByIdForUpdate(UUID cartId);

	Optional<CartEntity> findByUserIdAndActiveTrueForUpdate(Long userId);
	
}
