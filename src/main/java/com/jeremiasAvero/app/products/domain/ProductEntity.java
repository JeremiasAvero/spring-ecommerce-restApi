package com.jeremiasAvero.app.products.domain;

import com.jeremiasAvero.app.brand.domain.BrandEntity;
import com.jeremiasAvero.app.category.domain.CategoryEntity;
import com.jeremiasAvero.app.image.domain.ImageEntity;
import jakarta.persistence.*;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(
        name = "products",
        indexes = {
                @Index(name = "idx_products_name", columnList = "name"),
                @Index(name = "idx_products_price", columnList = "price")
        }
)
public class ProductEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name= "name", unique = true,nullable = false, length = 200)
    private String name;

    @Column(name = "description", length = 1000)
    private String description;

    @Column(name = "stock", nullable = false)
    private Integer stock;

    @Column(name = "price", nullable = false, precision = 15, scale = 2)
    private BigDecimal price;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "category_id", nullable = false,
            foreignKey = @ForeignKey(name = "fk_product_category"))
    private CategoryEntity category;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "brand_id", nullable = false,
            foreignKey = @ForeignKey(name = "fk_product_brand"))
    private BrandEntity brand;

    @Version
    private long version;

    @OneToMany(
            mappedBy =  "product",
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    @OrderBy("position ASC")
    private List<ImageEntity> images = new ArrayList<>();
    public List<ImageEntity> getImages() { return images; }
    public void setImages(List<ImageEntity> images) { this.images = images; }
    public long getVersion() { return version; } // opcional si lo necesitás
    public void setVersion(long version) { this.version = version; }
    public void addImage(ImageEntity img){
        images.add(img);
        img.setProduct(this);
    }

    public void removeImage(ImageEntity img){
        images.remove(img);
        img.setProduct(null);
    }

    public ProductEntity(){}

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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getStock() {
        return stock;
    }

    public void setStock(Integer stock) {
        this.stock = stock;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public CategoryEntity getCategory() {
        return category;
    }

    public void setCategory(CategoryEntity category) {
        this.category = category;
    }

    public BrandEntity getBrand() {
        return brand;
    }

    public void setBrand(BrandEntity brand) {
        this.brand = brand;
    }
}
