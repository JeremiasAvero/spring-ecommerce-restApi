package com.jeremiasAvero.app.order.application;


import com.jeremiasAvero.app.cart.domain.CartEntity;
import com.jeremiasAvero.app.cart.domain.CartItem;
import com.jeremiasAvero.app.cart.domain.CartRepository;
import com.jeremiasAvero.app.order.domain.*;
import com.jeremiasAvero.app.order.infraestructure.in.dto.AddressDto;
import com.jeremiasAvero.app.order.infraestructure.in.dto.CreateOrderFromCartRequest;
import com.jeremiasAvero.app.order.infraestructure.out.OrderRepository;
import com.jeremiasAvero.app.products.domain.ProductEntity;
import com.jeremiasAvero.app.products.domain.ProductRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class OrderService {

    private final CartRepository cartRepo;
    private final ProductRepository productRepo;
    private final OrderRepository orderRepo;
    private final OrderNumberService orderNumberService;

    public OrderService(CartRepository cartRepo,
                        ProductRepository productRepo,
                        OrderRepository orderRepo,
                        OrderNumberService orderNumberService) {
        this.cartRepo = cartRepo;
        this.productRepo = productRepo;
        this.orderRepo = orderRepo;
        this.orderNumberService = orderNumberService;
    }

    @Transactional
    public OrderEntity createFromCart(CreateOrderFromCartRequest req, Long currentUserIdOrNull) {
        // Idempotencia: 1 orden por cartId
        if (orderRepo.existsByCartId(req.getCartId())) {
            throw new IllegalStateException("Ya existe una orden para este carrito");
        }

        CartEntity cart = cartRepo.findById(req.getCartId())
                .orElseThrow(() -> new EntityNotFoundException("Carrito no encontrado o inactivo"));

        if (cart.getItems() == null || cart.getItems().isEmpty()) {
            throw new IllegalStateException("El carrito está vacío");
        }

        // Traer productos y validar stock
        Set<Long> productIds = cart.getItems().stream().map(CartItem::getProductId).collect(Collectors.toSet());
        Map<Long, ProductEntity> productsById = productRepo.findAllById(productIds).stream()
                .collect(Collectors.toMap(ProductEntity::getId, p -> p));
        System.out.println(productIds);
        System.out.println(productsById);
        // Calcular totales y construir Order + Items (snapshot)
        OrderEntity order = new OrderEntity();
        order.setOrderNumber(orderNumberService.nextNumber());
        order.setCartId(cart.getId());
        order.setUserId(currentUserIdOrNull);
        order.setCurrency("ARS");

        // buyer
        order.setBuyerEmail(req.getBuyerEmail());
        order.setBuyerName(req.getBuyerName());
        order.setBuyerPhone(req.getBuyerPhone());

        // addresses
        var ship = mapAddress(req.getShippingAddress());
        order.setShippingAddress(ship);
        if (req.isBillingSameAsShipping() || req.getBillingAddress() == null) {
            order.setBillingAddress(ship);
        } else {
            order.setBillingAddress(mapAddress(req.getBillingAddress()));
        }

        BigDecimal subtotal = BigDecimal.ZERO;
        for (CartItem ci : cart.getItems()) {
            ProductEntity p = productsById.get(ci.getProductId());
            if (p == null) throw new EntityNotFoundException("Producto " + ci.getProductId() + " no existe");

            // stock
            if (p.getStock() == null || p.getStock() < ci.getQty()) {
                throw new IllegalStateException("Stock insuficiente para: " + p.getName());
            }

            BigDecimal unitPrice = p.getPrice().setScale(2, RoundingMode.HALF_UP);
            BigDecimal line = unitPrice.multiply(BigDecimal.valueOf(ci.getQty()))
                    .setScale(2, RoundingMode.HALF_UP);

            OrderItem oi = new OrderItem();
            oi.setOrder(order);
            oi.setProductId(p.getId());
            oi.setProductName(p.getName());
            oi.setSku(null); // si luego agregás SKU en ProductEntity, mapear acá
            oi.setUnitPrice(unitPrice);
            oi.setQty(ci.getQty());
            oi.setLineTotal(line);
            order.addItem(oi);

            subtotal = subtotal.add(line);
        }

        // Pricing (podés mover a un PricingService si tenés impuestos/envío/reglas)
        BigDecimal discount = cart.getDiscount() != null ? cart.getDiscount() : BigDecimal.ZERO;
        BigDecimal tax = cart.getTax() != null ? cart.getTax() : BigDecimal.ZERO;
        BigDecimal shipping = cart.getShipping() != null ? cart.getShipping() : BigDecimal.ZERO;

        subtotal = subtotal.setScale(2, RoundingMode.HALF_UP);
        discount = discount.setScale(2, RoundingMode.HALF_UP);
        tax = tax.setScale(2, RoundingMode.HALF_UP);
        shipping = shipping.setScale(2, RoundingMode.HALF_UP);

        BigDecimal total = subtotal.subtract(discount).add(tax).add(shipping).setScale(2, RoundingMode.HALF_UP);

        order.setSubtotal(subtotal);
        order.setDiscount(discount);
        order.setTax(tax);
        order.setShipping(shipping);
        order.setTotal(total);

        // Persistir orden (cascade items)
        order = orderRepo.save(order);

        // Descontar stock
        for (CartItem ci : cart.getItems()) {
            ProductEntity p = productsById.get(ci.getProductId());
            p.setStock(p.getStock() - ci.getQty());
            productRepo.save(p);
        }

        // Cerrar carrito
        cart.setActive(false);
        cartRepo.save(cart);

        return order;
    }

    private com.jeremiasAvero.app.order.domain.Address mapAddress(AddressDto dto) {
        var a = new com.jeremiasAvero.app.order.domain.Address();
        a.setStreet(dto.getStreet());
        a.setCity(dto.getCity());
        a.setState(dto.getState());
        a.setZip(dto.getZip());
        a.setCountry(dto.getCountry());
        return a;
    }

    @Transactional
    public void markPaid(String orderNumber) {
        OrderEntity order = orderRepo.findByOrderNumber(orderNumber)
                .orElseThrow(() -> new EntityNotFoundException("Orden no encontrada: " + orderNumber));

        switch (order.getStatus()) {
            case CANCELLED, REFUNDED -> throw new IllegalStateException("No se puede marcar como pagada una orden cancelada o reembolsada");
            default -> {
            }
        }

        order.setPaymentStatus(PaymentStatus.CAPTURED);
        order.setStatus(OrderStatus.PAID);

        orderRepo.save(order);
    }

    @Transactional
    public void cancel(String orderNumber) {
        OrderEntity order = orderRepo.findByOrderNumber(orderNumber)
                .orElseThrow(() -> new EntityNotFoundException("Orden no encontrada: " + orderNumber));

        if (order.getStatus() == OrderStatus.CANCELLED) {
            throw new IllegalStateException("La orden ya está cancelada");
        }
        if (order.getStatus() == OrderStatus.REFUNDED) {
            throw new IllegalStateException("La orden ya fue reembolsada");
        }

        order.setStatus(OrderStatus.CANCELLED);

        for (OrderItem item : order.getItems()) {
            ProductEntity p = productRepo.findById(item.getProductId())
                    .orElseThrow(() -> new EntityNotFoundException("Producto " + item.getProductId() + " no existe"));
            p.setStock(p.getStock() + item.getQty());
            productRepo.save(p);
        }

        orderRepo.save(order);
    }

    @Transactional
    public void fulfill(String orderNumber) {
        OrderEntity order = orderRepo.findByOrderNumber(orderNumber)
                .orElseThrow(() -> new EntityNotFoundException("Orden no encontrada: " + orderNumber));

        if (order.getStatus() != OrderStatus.PAID) {
            throw new IllegalStateException("Solo se pueden marcar como cumplidas órdenes pagadas");
        }

        order.setFulfillmentStatus(FulfillmentStatus.FULFILLED);

        orderRepo.save(order);
    }

}