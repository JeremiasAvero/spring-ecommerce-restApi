package com.jeremiasAvero.app.image.domain;

import com.jeremiasAvero.app.products.domain.ProductEntity;
import jakarta.persistence.*;

@Entity
@Table(
        name = "images",
        indexes = { @Index(name = "idx_images_product", columnList = "product_id") }
)
public class ImageEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(
            name = "product_id",
            nullable = false,
            foreignKey = @ForeignKey(name = "fk_image_product")
    )
    private ProductEntity product;

    @Column(name = "image_url", nullable = false, length = 500)
    private String imageUrl;

    @Column(name = "alt_text", length = 255)
    private String altText;

    @Column(nullable = false)
    private int position = 0;

    @Column(name = "is_primary", nullable = false)
    private boolean primaryImage = false;

    public Long getId() { return id; }
    public ProductEntity getProduct() { return product; }
    public void setProduct(ProductEntity product) { this.product = product; }
    public String getImageUrl() { return imageUrl; }
    public void setImageUrl(String imageUrl) { this.imageUrl = imageUrl; }
    public String getAltText() { return altText; }
    public void setAltText(String altText) { this.altText = altText; }
    public int getPosition() { return position; }
    public void setPosition(int position) { this.position = position; }
    public boolean isPrimaryImage() { return primaryImage; }
    public void setPrimaryImage(boolean primaryImage) { this.primaryImage = primaryImage; }
}
