package com.jeremiasAvero.app.brand.infraestructure.ports.in.mapper;

import com.jeremiasAvero.app.brand.domain.BrandEntity;
import com.jeremiasAvero.app.brand.infraestructure.ports.in.dto.BrandResponseDto;
import com.jeremiasAvero.app.brand.infraestructure.ports.in.dto.CreateBrandRequestDto;
import com.jeremiasAvero.app.brand.infraestructure.ports.in.dto.UpdateBrandRequestDto;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.List;
import java.util.Objects;

@Component
public class BrandWebMapper {

    public BrandResponseDto toResponseDto(BrandEntity b) {
        if (b == null) return null;
        BrandResponseDto dto = new BrandResponseDto();
        dto.setId(b.getId());
        dto.setName(b.getName());

        return dto;
    }

    public List<BrandResponseDto> toResponseList(Collection<BrandEntity> brands){
        return brands == null ? List.of()
                : brands.stream().filter(Objects::nonNull).map(this::toResponseDto).toList();
    }

    // Domain Page -> DTO Page
    public Page<BrandResponseDto> toResponsePage(Page<BrandEntity> page) {
        return page.map(this::toResponseDto);
    }

    public BrandEntity toEntity(CreateBrandRequestDto req){
        BrandEntity b = new BrandEntity();
        b.setName(req.getName());
        return b;
    }

    public void updateEntity(BrandEntity target,
                             UpdateBrandRequestDto req
                             ) {
        if (target == null || req == null) return;
        target.setName(req.getName());
    }
}
