package com.jeremiasAvero.app.cart.application;

import java.math.BigDecimal;
import java.util.UUID;

import com.jeremiasAvero.app.cart.application.exception.ItemNotFoundException;
import com.jeremiasAvero.app.cart.application.exception.NotEnoughStockException;
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

	//OBTENER SI YA EXISTE O CREAR SI NO HAY REGISTRO
	public CartEntity getOrCreate(@Nullable UUID cartId, @Nullable Long userId) {
		//hay parametro usuario ?
		if (userId != null)
			//si existe usuario
			 return cartRepo
					 //busca carrito con id del usuario
					 .findByUserIdAndActiveTrue(userId)
					 //si no lo encuentra lo crea con el id del parametro
					 .orElseGet(() -> cartRepo.save(new CartEntity(userId)));

		//no hay idusuario pero parametro idcarrito ?
		if (cartId != null)
			//lo busca
			 return cartRepo.
					 findById(cartId).orElseThrow(); // o crear

		//sino crea uno nuevo sin usuario
		return cartRepo.save(new CartEntity());
	}
	
	//AGREGAR ITEM A CARRITO, SI YA EXISTE SUMA CANTIDAD
	public CartEntity addItem(UUID cartId, Long productId, int qty)
	{
		CartEntity cart = cartRepo.findByIdForUpdate(cartId);
		var p = productRepo.findById(productId).orElseThrow();

		if(qty > p.getStock()) throw new NotEnoughStockException();

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
		recalc(cart);
		return cartRepo.save(cart);
	}

	//SOLO MODIFICAR CANTIDAD DE ITEM YA EXISTENTE EN CARRITO
	public CartEntity updateQty(UUID cartId, Long itemId, int qty) {
		    CartEntity cart = cartRepo.findByIdForUpdate(cartId);
		    cart.getItems().stream().filter(i -> i.getProductId().equals(itemId)).findFirst()
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

	//MERGEAR DATOS DE CARRITO DE INVITADO CON USUARIO LOGUEADO
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

	//ELIMINAR ITEM DE CARRITO
	public CartEntity removeItem(UUID cartId, Long productId) {
		CartEntity cart = cartRepo.findByIdForUpdate(cartId);
		boolean removed = cart.getItems()
				.removeIf(i -> i.getProductId().equals(productId));

		if(!removed) throw new ItemNotFoundException();

		return cartRepo.save(cart);
	}

}
