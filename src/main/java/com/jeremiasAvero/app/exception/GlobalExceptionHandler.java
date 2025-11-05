package com.jeremiasAvero.app.exception;

import com.jeremiasAvero.app.brand.application.exception.BrandAlreadyExistsException;
import com.jeremiasAvero.app.brand.application.exception.BrandNotFoundException;
import com.jeremiasAvero.app.cart.application.exception.ItemNotFoundException;
import com.jeremiasAvero.app.cart.application.exception.NotEnoughStockException;
import com.jeremiasAvero.app.category.application.exception.CategoryAlreadyExistsException;
import com.jeremiasAvero.app.category.application.exception.CategoryNotFoundException;
import jakarta.persistence.OptimisticLockException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.ConstraintViolationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.OffsetDateTime;
import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {
    private static final Logger log =
            LoggerFactory.getLogger(GlobalExceptionHandler.class);
    //CART
    @ExceptionHandler(ItemNotFoundException.class)
    public ResponseEntity<ApiError> handleItemNotFound(ItemNotFoundException ex, HttpServletRequest req){
        return build(HttpStatus.NOT_FOUND, "CART_ITEM_NOT_FOUND", ex.getMessage(), req.getRequestURI(), null);


    }
    @ExceptionHandler(NotEnoughStockException.class)
    public ResponseEntity<ApiError> handleNotEnoughStock(NotEnoughStockException ex, HttpServletRequest req) {
        return build(HttpStatus.CONFLICT, "NOT_ENOUGH_STOCK", ex.getMessage(), req.getRequestURI(), null);
    }

    //BRAND
    @ExceptionHandler(BrandAlreadyExistsException.class)
    public ResponseEntity<ApiError> handleBrandExists(BrandAlreadyExistsException ex, HttpServletRequest req) {
        return build(HttpStatus.CONFLICT, "BRAND_ALREADY_EXISTS", ex.getMessage(), req.getRequestURI(), null);
    }

    @ExceptionHandler(BrandNotFoundException.class)
    public ResponseEntity<ApiError> handleBrandNotFound(BrandNotFoundException ex, HttpServletRequest req) {
        return build(HttpStatus.NOT_FOUND, "BRAND_NOT_FOUND", ex.getMessage(), req.getRequestURI(), null);
    }
    //CATEGORY
    @ExceptionHandler(CategoryAlreadyExistsException.class)
    public ResponseEntity<ApiError> handleBrandExists(CategoryAlreadyExistsException ex, HttpServletRequest req) {
        return build(HttpStatus.CONFLICT, "CATEGORY_ALREADY_EXISTS", ex.getMessage(), req.getRequestURI(), null);
    }

    @ExceptionHandler(CategoryNotFoundException.class)
    public ResponseEntity<ApiError> handleBrandNotFound(CategoryNotFoundException ex, HttpServletRequest req) {
        return build(HttpStatus.NOT_FOUND, "CATEGORY_NOT_FOUND", ex.getMessage(), req.getRequestURI(), null);
    }

    // --- VALIDACIONES ---
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiError> handleValidation(MethodArgumentNotValidException ex, HttpServletRequest req) {
        Map<String, Object> details = new HashMap<>();
        Map<String, String> fields = new HashMap<>();
        for (FieldError fe : ex.getBindingResult().getFieldErrors()) {
            fields.put(fe.getField(), fe.getDefaultMessage());
        }
        details.put("fields", fields);
        return build(HttpStatus.BAD_REQUEST, "VALIDATION_ERROR", "Invalid request data", req.getRequestURI(), details);
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ApiError> handleConstraint(ConstraintViolationException ex, HttpServletRequest req) {
        Map<String, Object> details = new HashMap<>();
        details.put("violations", ex.getConstraintViolations().stream().map(v ->
                Map.of("property", v.getPropertyPath().toString(), "message", v.getMessage())
        ).toList());
        return build(HttpStatus.BAD_REQUEST, "CONSTRAINT_VIOLATION", "Constraint violation", req.getRequestURI(), details);
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ApiError> handleNotReadable(HttpMessageNotReadableException ex, HttpServletRequest req) {
        return build(HttpStatus.BAD_REQUEST, "MALFORMED_JSON", "Malformed JSON or invalid body", req.getRequestURI(), null);
    }

    // --- CONCURRENCIA / BD ---
    @ExceptionHandler(OptimisticLockException.class)
    public ResponseEntity<ApiError> handleOptimistic(OptimisticLockException ex, HttpServletRequest req) {
        return build(HttpStatus.CONFLICT, "CONCURRENCY_CONFLICT", "Resource was modified concurrently", req.getRequestURI(), null);
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<ApiError> handleDataIntegrity(DataIntegrityViolationException ex, HttpServletRequest req) {
        return build(HttpStatus.CONFLICT, "DATA_INTEGRITY_ERROR", "Data integrity violation", req.getRequestURI(), null);
    }

    // --- FALLBACK ---
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiError> handleAll(Exception ex, HttpServletRequest req) {
        log.error("Unhandled error", ex);
        return build(HttpStatus.INTERNAL_SERVER_ERROR, "INTERNAL_ERROR", "Unexpected error", req.getRequestURI(), null);
    }

    private ResponseEntity<ApiError> build(
            HttpStatus status,
            String code,
            String message,
            String path,
            Map<String,Object> details){
        ApiError body = new ApiError(
                OffsetDateTime.now(),
        status.value(),
        status.getReasonPhrase(),
        code, message, path, details);

        return ResponseEntity.status(status).body(body);
    }
}
