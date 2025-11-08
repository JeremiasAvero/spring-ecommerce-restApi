package com.jeremiasAvero.app.order.infraestructure.out;

import com.jeremiasAvero.app.order.domain.OrderEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface OrderRepository extends JpaRepository<OrderEntity, UUID> {
    boolean existsByCartId(UUID cartId);
    Optional<OrderEntity> findByOrderNumber(String orderNumber);
}
