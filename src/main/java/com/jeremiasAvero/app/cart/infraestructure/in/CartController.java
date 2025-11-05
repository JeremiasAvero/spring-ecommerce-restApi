package com.jeremiasAvero.app.cart.infraestructure.in;

import java.util.UUID;

import com.jeremiasAvero.app.cart.infraestructure.in.dto.*;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import com.jeremiasAvero.app.auth.domain.AppUser;
import com.jeremiasAvero.app.cart.application.CartService;
import com.jeremiasAvero.app.cart.domain.CartEntity;

@RestController
@RequestMapping("/api/cart")
public class CartController {

	private final CartService cartService;
	private final CartMapper cartMapper;
	
	public CartController(CartService cartService, CartMapper cartMapper) {
		this.cartService = cartService;
		this.cartMapper = cartMapper;
	}

	@GetMapping
	public  ResponseEntity<CartDto> getCart(
			@RequestHeader(value="X-Cart-Id", required = false) UUID cartId,
			  @AuthenticationPrincipal AppUser user) {
		System.out.println(user.getUsername());
		 var cart = cartService.getOrCreate(cartId,  user.getId());
		    return ResponseEntity.ok()
		        .header("X-Cart-Id", cart.getId().toString())
		        .body(cartMapper.toCartDto(cart));
	}


	@PostMapping("/items")
	public ResponseEntity<CartDto> add(@RequestHeader(value="X-Cart-Id", required=false) UUID cartId,
						  @AuthenticationPrincipal AppUser user,
						  @RequestBody AddItemReq req,
						  @RequestHeader(value="Idempotency-Key", required=false) String idem) {
		var base = cartService.getOrCreate(cartId, user != null ? user.getId() : null);
		var updated = cartService.addItem(base.getId(), req.productId(), req.qty());
		return ResponseEntity.ok().body(cartMapper.toCartDto(updated));
	}

	@PutMapping("/items")
	public ResponseEntity<CartDto> updateQty(@RequestHeader(value="X-Cart-Id", required=false) UUID cartId,
									   @AuthenticationPrincipal AppUser user,
									   @RequestBody AddItemReq req,
									   @RequestHeader(value="Idempotency-Key", required=false) String idem) {
		var base = cartService.getOrCreate(cartId, user != null ? user.getId() : null);
		var updated = cartService.updateQty(base.getId(), req.productId(), req.qty());
		return ResponseEntity.ok().body(cartMapper.toCartDto(updated));
	}


	@PostMapping("/merge")
	public CartEntity merge(@RequestBody MergeReq req,
			  @AuthenticationPrincipal AppUser user) {
	    var merged = cartService.merge(req.guestCartId(), user.getId());
	    return (CartEntity)merged;
	}

	@DeleteMapping("/items")
	public ResponseEntity<CartDto> deleteItem(
			@RequestHeader(value = "X-Cart-Id", required = false) UUID cartId,
			@AuthenticationPrincipal AppUser user,
			@RequestBody DeleteItemReq req)
	{
		var base = cartService.getOrCreate(cartId, user != null ? user.getId() : null);
		var deleted = cartService.removeItem(base.getId(), req.itemId());

		return ResponseEntity.ok().body(cartMapper.toCartDto(deleted));
	}
}
