package com.jeremiasAvero.app.category.domain;

import com.jeremiasAvero.app.products.domain.ProductEntity;
import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "categories")
public class CategoryEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "name", unique = true, nullable = false, length = 150)
    private String name;

    @OneToMany(mappedBy = "category")
    private List<ProductEntity> product = new ArrayList<>();

    public CategoryEntity(){}

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<ProductEntity> getProduct() {
        return product;
    }

    public void setProduct(List<ProductEntity> product) {
        this.product = product;
    }
}
