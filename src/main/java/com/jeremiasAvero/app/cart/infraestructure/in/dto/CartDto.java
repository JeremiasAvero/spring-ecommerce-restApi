package com.jeremiasAvero.app.cart.infraestructure.in.dto;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

public record CartDto(
		java.util.UUID id, 
		Long userId, 
		boolean active,
        java.math.BigDecimal subtotal, 
        java.math.BigDecimal discount,
        java.math.BigDecimal tax, 
        java.math.BigDecimal shipping,
        java.math.BigDecimal total, 
        java.util.List<CartItemDto> items
		) {
}
