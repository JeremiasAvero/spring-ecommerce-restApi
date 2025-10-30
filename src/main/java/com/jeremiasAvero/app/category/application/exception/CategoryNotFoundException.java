package com.jeremiasAvero.app.category.application.exception;

public class CategoryNotFoundException extends RuntimeException {
    public CategoryNotFoundException(Long id) {

        super("Category not found: " + id);
    }
}
