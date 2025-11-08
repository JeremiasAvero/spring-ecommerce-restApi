package com.jeremiasAvero.app.order.infraestructure.in.dto;


import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import java.util.UUID;

public class CreateOrderFromCartRequest {
    @NotNull private UUID cartId;

    @Email @NotBlank private String buyerEmail;
    @NotBlank        private String buyerName;
    @Size(max=40)    private String buyerPhone;

    @Valid @NotNull  private AddressDto shippingAddress;
    @Valid           private AddressDto billingAddress; // si null, se copia shipping
    private boolean billingSameAsShipping = true;

    public @NotNull UUID getCartId() {
        return cartId;
    }

    public void setCartId(@NotNull UUID cartId) {
        this.cartId = cartId;
    }

    public @Email @NotBlank String getBuyerEmail() {
        return buyerEmail;
    }

    public void setBuyerEmail(@Email @NotBlank String buyerEmail) {
        this.buyerEmail = buyerEmail;
    }

    public @NotBlank String getBuyerName() {
        return buyerName;
    }

    public void setBuyerName(@NotBlank String buyerName) {
        this.buyerName = buyerName;
    }

    public @Size(max = 40) String getBuyerPhone() {
        return buyerPhone;
    }

    public void setBuyerPhone(@Size(max = 40) String buyerPhone) {
        this.buyerPhone = buyerPhone;
    }

    public @Valid @NotNull AddressDto getShippingAddress() {
        return shippingAddress;
    }

    public void setShippingAddress(@Valid @NotNull AddressDto shippingAddress) {
        this.shippingAddress = shippingAddress;
    }

    public @Valid AddressDto getBillingAddress() {
        return billingAddress;
    }

    public void setBillingAddress(@Valid AddressDto billingAddress) {
        this.billingAddress = billingAddress;
    }

    public boolean isBillingSameAsShipping() {
        return billingSameAsShipping;
    }

    public void setBillingSameAsShipping(boolean billingSameAsShipping) {
        this.billingSameAsShipping = billingSameAsShipping;
    }
}