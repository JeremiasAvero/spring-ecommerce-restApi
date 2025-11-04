package com.jeremiasAvero.app.cart.infraestructure.out;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.jeremiasAvero.app.cart.domain.CartEntity;

import jakarta.persistence.LockModeType;

@Repository
public interface CartJpaRepository extends JpaRepository<CartEntity, UUID>{

	  @EntityGraph(attributePaths = {"items"})
	  Optional<CartEntity> findByUserId(Long userId);

	  @EntityGraph(attributePaths = {"items"})
	  Optional<CartEntity> findById(UUID id);

	  @Lock(LockModeType.PESSIMISTIC_WRITE)
	  @Query("select c from CartEntity c left join fetch c.items where c.id = :id")
	  Optional<CartEntity> findByIdForUpdate(@Param("id") UUID id);

	  @Lock(LockModeType.PESSIMISTIC_WRITE)
	  @Query("select c from CartEntity c left join fetch c.items where c.userId = :userId and c.active = true")
	  Optional<CartEntity> findByUserIdAndActiveTrueForUpdate(@Param("userId") Long userId);
}
