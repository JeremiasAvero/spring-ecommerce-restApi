package com.jeremiasAvero.app.order.infraestructure.in;

import com.jeremiasAvero.app.auth.domain.AppUser;
import com.jeremiasAvero.app.order.application.OrderService;
import com.jeremiasAvero.app.order.domain.OrderEntity;
import com.jeremiasAvero.app.order.infraestructure.in.dto.CreateOrderFromCartRequest;
import com.jeremiasAvero.app.order.infraestructure.in.dto.OrderItemResponse;
import com.jeremiasAvero.app.order.infraestructure.in.dto.OrderResponse;
import com.jeremiasAvero.app.order.infraestructure.in.mapper.OrderMapper;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/orders")
public class OrderController {
    private final OrderService orderService;
    private final OrderMapper orderMapper;

    public OrderController(
            OrderService orderService
    ,OrderMapper orderMapper) {
        this.orderService = orderService;
        this.orderMapper = orderMapper;
    }

    @PostMapping("/from-cart")
    public ResponseEntity<OrderResponse> createFromCart(
            @Valid @RequestBody CreateOrderFromCartRequest req,
            @AuthenticationPrincipal AppUser user) {
        Long userIdOrNull = user.getId();
        OrderEntity order = orderService.createFromCart(req, userIdOrNull);
        return ResponseEntity.status(201).body(orderMapper.toResponse(order));
    }

    @PatchMapping("/{orderNumber}/mark-paid")
    public ResponseEntity<Void> markPaid(@PathVariable String orderNumber){
        orderService.markPaid(orderNumber);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{orderNumber}/cancel")
    public ResponseEntity<Void> cancel(@PathVariable String orderNumber){
        orderService.cancel(orderNumber);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{orderNumber}/fulfill")
    public ResponseEntity<Void> fulfill(@PathVariable String orderNumber){
        orderService.fulfill(orderNumber);
        return ResponseEntity.noContent().build();
    }
}
