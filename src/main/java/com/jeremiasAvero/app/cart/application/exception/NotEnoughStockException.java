package com.jeremiasAvero.app.cart.application.exception;

public class NotEnoughStockException extends RuntimeException {
    public NotEnoughStockException() {

    super("Item Stock not enough");
}
}
