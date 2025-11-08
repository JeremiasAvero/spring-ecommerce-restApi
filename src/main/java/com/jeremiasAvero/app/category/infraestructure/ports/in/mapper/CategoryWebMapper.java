package com.jeremiasAvero.app.category.infraestructure.ports.in.mapper;

import com.jeremiasAvero.app.category.domain.CategoryEntity;
import com.jeremiasAvero.app.category.infraestructure.ports.in.dto.CategoryResponseDto;
import com.jeremiasAvero.app.category.infraestructure.ports.in.dto.CreateCategoryRequestDto;
import com.jeremiasAvero.app.category.infraestructure.ports.in.dto.UpdateCategoryRequestDto;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.List;
import java.util.Objects;

@Component
public class CategoryWebMapper {

    public CategoryResponseDto toResponseDto(CategoryEntity c) {
        CategoryResponseDto dto = new CategoryResponseDto();
        dto.setId(c.getId());
        dto.setName(c.getName());
        return dto;
    }

    public List<CategoryResponseDto> toResponseList(Collection<CategoryEntity> categories) {
        return categories == null ? List.of()
                : categories.stream().filter(Objects::nonNull).map(this::toResponseDto).toList();
    }

    public Page<CategoryResponseDto> toResponsePage(Page<CategoryEntity> page) {
        return page.map(this::toResponseDto);
    }

    public CategoryEntity toEntity(CreateCategoryRequestDto req) {
        CategoryEntity c = new CategoryEntity();
        c.setName(req.getName());
        return c;
    }

    public void updateEntity(CategoryEntity target,
                             UpdateCategoryRequestDto req
    ) {
        if (target == null || req == null) return;
        target.setName(req.getName());

    }
}