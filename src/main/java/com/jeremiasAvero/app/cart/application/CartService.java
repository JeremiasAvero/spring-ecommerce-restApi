package com.jeremiasAvero.app.cart.application;

import java.math.BigDecimal;
import java.util.UUID;

import org.springframework.stereotype.Service;

import com.jeremiasAvero.app.cart.domain.CartEntity;
import com.jeremiasAvero.app.cart.domain.CartItem;
import com.jeremiasAvero.app.cart.domain.CartRepository;
import com.jeremiasAvero.app.products.domain.ProductRepository;

import jakarta.annotation.Nullable;
import jakarta.transaction.Transactional;

@Service
@Transactional
public class CartService {

	private final CartRepository cartRepo;
	private final ProductRepository productRepo;
	
	public CartService(CartRepository cartRepo, ProductRepository productRepo) {
		this.cartRepo = cartRepo;
		this.productRepo = productRepo;
	}
	
	public CartEntity getOrCreate(@Nullable UUID cartId, @Nullable Long userId) {
		 if (userId != null) return cartRepo.findByUserIdAndActiveTrue(userId).orElseGet(() -> cartRepo.save(new CartEntity(/*userId*/)));
		 if (cartId != null) return cartRepo.findById(cartId).orElseThrow(); // o crear
		 return cartRepo.save(new CartEntity());
	}
	
	
	public CartEntity addItem(UUID cartId, Long productId, int qty)
	{
		CartEntity cart = cartRepo.findByIdForUpdate(cartId);
		var p = productRepo.findById(productId).orElseThrow();
		var item = cart.getItems().stream().filter(i -> i.getProductId().equals(productId)).findFirst()
				.orElseGet(() -> {
					var it = new CartItem();
					it.setCart(cart);
					it.setProductId(productId);
					it.setUnitPrice(p.getPrice());
					cart.getItems().add(it);
					return it;
				});
		
		item.setQty(Math.max(1, item.getQty() + qty));
		return cartRepo.save(cart);
	}
	
	 public CartEntity updateQty(UUID cartId, Long itemId, int qty) {
		    CartEntity cart = cartRepo.findByIdForUpdate(cartId);
		    cart.getItems().stream().filter(i -> i.getId().equals(itemId)).findFirst()
		        .ifPresent(i -> i.setQty(qty));
		    recalc(cart);
		    return cartRepo.save(cart);
		  }

	  private void recalc(CartEntity cart) {
	    BigDecimal subtotal = cart.getItems().stream()
	      .map(i -> i.getUnitPrice().multiply(BigDecimal.valueOf(i.getQty())))
	      .reduce(BigDecimal.ZERO, BigDecimal::add);
	    cart.setSubtotal(subtotal);
	    cart.setDiscount(BigDecimal.ZERO);
	    cart.setTax(BigDecimal.ZERO);    
	    cart.setShipping(BigDecimal.ZERO); 
	    cart.setTotal(subtotal.subtract(cart.getDiscount()).add(cart.getTax()).add(cart.getShipping()));
	  }

	  public CartEntity merge(UUID guestCartId, Long userId) {
	    CartEntity guest = cartRepo.findByIdForUpdate(guestCartId);
	    CartEntity userCart = cartRepo.findByUserIdAndActiveTrueForUpdate(userId).orElseGet(() -> {
	      var c = new CartEntity();
	      c.setUserId(userId);
	      return cartRepo.save(c);
	    });
	    for (var gi : guest.getItems()) {
	      addItem(userCart.getId(), gi.getProductId(), gi.getQty());
	    }
	    // opcional: marcar guest como CLOSED
	    return userCart;
	  }
	
}
