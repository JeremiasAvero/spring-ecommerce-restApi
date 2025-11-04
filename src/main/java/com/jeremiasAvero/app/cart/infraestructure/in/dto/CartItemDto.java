package com.jeremiasAvero.app.cart.infraestructure.in.dto;

public record CartItemDto(
		Long id, 
		Long productId, 
		int qty,
        java.math.BigDecimal unitPrice, 
        java.math.BigDecimal lineTotal) {

}
