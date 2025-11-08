package com.jeremiasAvero.app.order.infraestructure.in.mapper;

import com.jeremiasAvero.app.order.domain.OrderEntity;
import com.jeremiasAvero.app.order.infraestructure.in.dto.OrderItemResponse;
import com.jeremiasAvero.app.order.infraestructure.in.dto.OrderResponse;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;

@Component
public class OrderMapper {

    public OrderResponse toResponse(OrderEntity o) {
        var r = new OrderResponse();
        r.setId(o.getId());
        r.setOrderNumber(o.getOrderNumber());
        r.setStatus(o.getStatus());
        r.setPaymentStatus(o.getPaymentStatus());
        r.setFulfillmentStatus(o.getFulfillmentStatus());
        r.setCurrency(o.getCurrency());
        r.setSubtotal(o.getSubtotal());
        r.setDiscount(o.getDiscount());
        r.setTax(o.getTax());
        r.setShipping(o.getShipping());
        r.setTotal(o.getTotal());
        r.setBuyerEmail(o.getBuyerEmail());
        r.setBuyerName(o.getBuyerName());
        r.setBuyerPhone(o.getBuyerPhone());
        r.setCreatedAt(o.getCreatedAt());

        r.setItems(o.getItems().stream().map(oi -> {
            var it = new OrderItemResponse();
            it.setProductId(oi.getProductId());
            it.setProductName(oi.getProductName());
            it.setSku(oi.getSku());
            it.setQty(oi.getQty());
            it.setUnitPrice(oi.getUnitPrice());
            it.setLineTotal(oi.getLineTotal());
            return it;
        }).collect(Collectors.toList()));
        return r;
    }

}
