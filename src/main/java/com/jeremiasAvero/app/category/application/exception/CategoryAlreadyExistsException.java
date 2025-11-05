package com.jeremiasAvero.app.category.application.exception;

public class CategoryAlreadyExistsException extends RuntimeException {
    public CategoryAlreadyExistsException(String name) {
        super("Category " + name +" already exists" );

    }
}
