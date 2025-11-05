package com.jeremiasAvero.app.brand.infraestructure.ports.in;

import com.jeremiasAvero.app.brand.application.BrandService;
import com.jeremiasAvero.app.brand.infraestructure.ports.in.dto.BrandResponseDto;
import com.jeremiasAvero.app.brand.infraestructure.ports.in.dto.CreateBrandRequestDto;
import com.jeremiasAvero.app.brand.infraestructure.ports.in.dto.UpdateBrandRequestDto;
import com.jeremiasAvero.app.brand.infraestructure.ports.in.mapper.BrandWebMapper;
import com.jeremiasAvero.app.exception.ApiError;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/brands")
public class BrandController {
    private final BrandService brandService;
    private final BrandWebMapper mapper;

    public BrandController(BrandService brandService, BrandWebMapper mapper){
        this.brandService = brandService;
        this.mapper = mapper;
    }

    @GetMapping
    public ResponseEntity<List<BrandResponseDto>> findAll(){
        var brands= brandService.findAll();
        if (brands.isEmpty()) return ResponseEntity.noContent().build();
       return ResponseEntity.ok(mapper.toResponseList(brands));
    }

    @PostMapping
    public ResponseEntity<?> create(
            @Valid @RequestBody CreateBrandRequestDto req){
        var entity = mapper.toEntity(req);
        var saved = brandService.save(entity);
        return ResponseEntity.status(201).body(mapper.toResponseDto(saved));
    }
    @PutMapping("/{id}")
    public ResponseEntity<?> update(
            @PathVariable Long id,
            @Valid @RequestBody UpdateBrandRequestDto req) {

    	var existing = brandService.findById(id); // o 404 si no existe

        mapper.updateEntity(existing, req);
        var saved = brandService.save(existing);
        return ResponseEntity.ok(mapper.toResponseDto(saved));
    }
}
