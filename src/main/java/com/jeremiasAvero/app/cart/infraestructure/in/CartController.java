package com.jeremiasAvero.app.cart.infraestructure.in;

import java.util.UUID;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.jeremiasAvero.app.auth.domain.AppUser;
import com.jeremiasAvero.app.cart.application.CartService;
import com.jeremiasAvero.app.cart.domain.CartEntity;
import com.jeremiasAvero.app.cart.infraestructure.in.dto.CartDto;
import com.jeremiasAvero.app.cart.infraestructure.in.dto.CartMapper;

@RestController
@RequestMapping("/api/cart")
public class CartController {

	private final CartService cartService;
	private final CartMapper cartMapper;
	
	public CartController(CartService cartService, CartMapper cartMapper) {
		this.cartService = cartService;
		this.cartMapper = cartMapper;
	}
	record AddItemReq(Long productId, int qty) {}
	record MergeReq(UUID guestCartId) {}

	@GetMapping
	public  ResponseEntity<CartDto> getCart(
			@RequestHeader(value="X-Cart-Id", required = false) UUID cartId,
			  @AuthenticationPrincipal AppUser user) {
		 var cart = cartService.getOrCreate(cartId, user != null ? user.getId() : null);
		    return ResponseEntity.ok()
		        .header("X-Cart-Id", cart.getId().toString())
		        .body(cartMapper.toCartDto(cart));
	}
	

	  @PostMapping("/items")
	  public CartEntity add(@RequestHeader(value="X-Cart-Id", required=false) UUID cartId,
	                     @AuthenticationPrincipal AppUser user,
	                     @RequestBody AddItemReq req,
	                     @RequestHeader(value="Idempotency-Key", required=false) String idem) {
	    var base = cartService.getOrCreate(cartId, user != null ? user.getId() : null);
	    var updated = cartService.addItem(base.getId(), req.productId(), req.qty());
	    return (CartEntity) updated;
	  }

	  @PostMapping("/merge")
	  public CartEntity merge(@RequestBody MergeReq req, 
			  @AuthenticationPrincipal AppUser user) {
	    var merged = cartService.merge(req.guestCartId(), user.getId());
	    return (CartEntity)merged;
	  }
}
