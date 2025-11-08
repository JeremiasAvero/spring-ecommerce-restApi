package com.jeremiasAvero.app.brand.application.exception;

public class BrandNotFoundException extends RuntimeException {
    public BrandNotFoundException(Long id, String name) {
        super("Brand not found with " + (id == null ? "name: " + name : "id: " + String.valueOf(id)));

    }
}
