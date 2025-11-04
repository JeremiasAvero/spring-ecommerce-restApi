package com.jeremiasAvero.app.cart.infraestructure.out;

import java.util.Optional;
import java.util.UUID;

import org.springframework.stereotype.Component;

import com.jeremiasAvero.app.cart.domain.CartEntity;
import com.jeremiasAvero.app.cart.domain.CartRepository;

@Component
public class CartRepositoryImpl implements CartRepository {
	private final CartJpaRepository cartRepo;
	
	public CartRepositoryImpl(CartJpaRepository cartRepo) {
		this.cartRepo = cartRepo;
	}

	@Override
	public Optional<CartEntity> findByUserIdAndActiveTrue(Long userId) {
		return cartRepo.findByUserId(userId);
	}

	@Override
	public Optional<CartEntity> findById(UUID cartId) {
		return cartRepo.findById(cartId);
	}

	@Override
	public CartEntity save(CartEntity cart) {
		return cartRepo.save(cart);
	}

	@Override
	public CartEntity findByIdForUpdate(UUID cartId) {
		return cartRepo.findByIdForUpdate(cartId).orElseThrow();
	}

	@Override
	public Optional<CartEntity> findByUserIdAndActiveTrueForUpdate(Long userId) {
		return cartRepo.findByUserIdAndActiveTrueForUpdate(userId);

	}
	
	

}
