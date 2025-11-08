package com.jeremiasAvero.app.products.infraestructure.in.dto;

import com.jeremiasAvero.app.image.infraestructure.ImageDto;

import java.math.BigDecimal;
import java.util.List;

public class ProductResponseDto {
    private Long id;
    private String name;
    private String description;
    private Integer stock;
    private BigDecimal price;

    private IdName category;
    private IdName brand;

    private List<ImageDto> images; // NUEVO
    public java.util.List<ImageDto> getImages() { return images; }
    public void setImages(java.util.List<ImageDto> images) { this.images = images; }
    public IdName getBrand() {
        return brand;
    }

    public void setBrand(IdName brand) {
        this.brand = brand;
    }

    public IdName getCategory() {
        return category;
    }

    public void setCategory(IdName category) {
        this.category = category;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public Integer getStock() {
        return stock;
    }

    public void setStock(Integer stock) {
        this.stock = stock;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }


    public static class IdName {
        private Long id;
        private String name;

        public IdName() {}
        public IdName(Long id, String name) { this.id = id; this.name = name; }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public Long getId() {
            return id;
        }

        public void setId(Long id) {
            this.id = id;
        }
    }
}
