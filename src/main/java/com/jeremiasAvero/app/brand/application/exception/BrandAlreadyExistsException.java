package com.jeremiasAvero.app.brand.application.exception;

public class BrandAlreadyExistsException extends RuntimeException {
    public BrandAlreadyExistsException(String name) {
        super("Brand " + name +" already exists" );

    }
}
