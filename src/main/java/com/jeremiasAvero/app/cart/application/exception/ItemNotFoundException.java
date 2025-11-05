package com.jeremiasAvero.app.cart.application.exception;

public class ItemNotFoundException extends RuntimeException {
    public ItemNotFoundException() {

    super("Item not found in Cart");
}
}
