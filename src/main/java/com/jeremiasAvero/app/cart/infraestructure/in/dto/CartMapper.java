package com.jeremiasAvero.app.cart.infraestructure.in.dto;

import java.math.BigDecimal;
import java.util.function.Function;

import org.springframework.stereotype.Component;

import com.jeremiasAvero.app.cart.domain.CartEntity;
import com.jeremiasAvero.app.cart.domain.CartItem;

@Component
public class CartMapper {
	public CartDto toCartDto(CartEntity c) {
		  var items = c.getItems().stream().map(this::toCartItemDto).toList();

		  // Si no calculás totales en el servicio, al menos evitá nulls:
		  Function<BigDecimal, BigDecimal> nvl = v -> v == null ? BigDecimal.ZERO : v;

		  BigDecimal subtotal = items.stream()
		        .map(CartItemDto::lineTotal)
		        .reduce(BigDecimal.ZERO, BigDecimal::add);

		  BigDecimal discount = nvl.apply(c.getDiscount());
		  BigDecimal tax      = nvl.apply(c.getTax());
		  BigDecimal shipping = nvl.apply(c.getShipping());
		  BigDecimal total    = c.getTotal() != null
		        ? c.getTotal()
		        : subtotal.subtract(discount).add(tax).add(shipping);

		  return new CartDto(
		        c.getId(), c.getUserId(), c.isActive(),
		        subtotal, discount, tax, shipping, total, items
		  );	
	}
	
	public CartItemDto toCartItemDto(CartItem it) {
		BigDecimal unit = 
				it.getUnitPrice() == null ? BigDecimal.ZERO : it.getUnitPrice();
		  BigDecimal line = unit.multiply(BigDecimal.valueOf(it.getQty()));
		  return new CartItemDto(
		    		it.getId(), it.getProductId(), it.getQty(), unit, line);
		 
	}
	
}
