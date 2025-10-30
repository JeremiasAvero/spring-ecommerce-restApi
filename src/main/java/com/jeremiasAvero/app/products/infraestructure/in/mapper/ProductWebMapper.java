package com.jeremiasAvero.app.products.infraestructure.in.mapper;

import com.jeremiasAvero.app.brand.domain.BrandEntity;
import com.jeremiasAvero.app.category.domain.CategoryEntity;
import com.jeremiasAvero.app.products.domain.ProductEntity;
import com.jeremiasAvero.app.products.infraestructure.in.dto.CreateProductRequestDto;
import com.jeremiasAvero.app.products.infraestructure.in.dto.ProductResponseDto;
import com.jeremiasAvero.app.products.infraestructure.in.dto.UpdateProductRequestDto;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.List;
import java.util.Objects;

@Component
public class ProductWebMapper {

    public ProductResponseDto toResponseDto(ProductEntity p) {
        if (p == null) return null;
        ProductResponseDto dto = new ProductResponseDto();
        dto.setId(p.getId());
        dto.setName(p.getName());
        dto.setDescription(p.getDescription());
        dto.setPrice(p.getPrice());
        dto.setStock(p.getStock());

        CategoryEntity c = p.getCategory();
        if(c != null){
            dto.setCategory(new ProductResponseDto.IdName(c.getId(),c.getName()));
        }
        BrandEntity b = p.getBrand();
        if(b != null){
            dto.setBrand(new ProductResponseDto.IdName(b.getId(),b.getName()));
        }
        return dto;
    }

    public List<ProductResponseDto> toResponseList(Collection<ProductEntity> products){
        return products == null ? List.of()
                : products.stream().filter(Objects::nonNull).map(this::toResponseDto).toList();
    }

    // Domain Page -> DTO Page
    public Page<ProductResponseDto> toResponsePage(Page<ProductEntity> page) {
        return page.map(this::toResponseDto);
    }

    public ProductEntity toEntity(
            CreateProductRequestDto req, CategoryEntity category, BrandEntity brand){
        ProductEntity p = new ProductEntity();
        p.setName(req.getName());
        p.setDescription(req.getDescription());
        p.setStock(req.getStock());
        p.setPrice(req.getPrice());
        p.setCategory(category);
        p.setBrand(brand);
        return p;
    }

    public void updateEntity(ProductEntity target,
                             UpdateProductRequestDto req,
                             CategoryEntity category,
                             BrandEntity brand) {
        if (target == null || req == null) return;
        target.setName(req.getName());
        target.setDescription(req.getDescription());
        target.setStock(req.getStock());
        target.setPrice(req.getPrice());
        target.setCategory(category);
        target.setBrand(brand);
    }
}