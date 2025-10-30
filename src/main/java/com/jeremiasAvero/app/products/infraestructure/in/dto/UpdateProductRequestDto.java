package com.jeremiasAvero.app.products.infraestructure.in.dto;

import jakarta.validation.constraints.*;

import java.math.BigDecimal;

public class UpdateProductRequestDto {
    @NotBlank
    @Size(max = 200)
    private String name;

    @Size(max = 1000)
    private String description;

    @NotNull
    @PositiveOrZero
    private Integer stock;

    @NotNull
    @DecimalMin(value = "0.00", inclusive = true)
    @Digits(integer = 13, fraction = 2)
    private BigDecimal price;

    @NotNull
    private Long categoryId;

    @NotNull
    private Long brandId;

    public @NotBlank @Size(max = 200) String getName() {
        return name;
    }

    public void setName(@NotBlank @Size(max = 200) String name) {
        this.name = name;
    }

    public @Size(max = 1000) String getDescription() {
        return description;
    }

    public void setDescription(@Size(max = 1000) String description) {
        this.description = description;
    }

    public @NotNull @PositiveOrZero Integer getStock() {
        return stock;
    }

    public void setStock(@NotNull @PositiveOrZero Integer stock) {
        this.stock = stock;
    }

    public @NotNull @DecimalMin(value = "0.00", inclusive = true) @Digits(integer = 13, fraction = 2) BigDecimal getPrice() {
        return price;
    }

    public void setPrice(@NotNull @DecimalMin(value = "0.00", inclusive = true) @Digits(integer = 13, fraction = 2) BigDecimal price) {
        this.price = price;
    }

    public @NotNull Long getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(@NotNull Long categoryId) {
        this.categoryId = categoryId;
    }

    public @NotNull Long getBrandId() {
        return brandId;
    }

    public void setBrandId(@NotNull Long brandId) {
        this.brandId = brandId;
    }
}
