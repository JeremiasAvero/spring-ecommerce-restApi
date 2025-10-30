package com.jeremiasAvero.app.category.infraestructure.ports.in;


import com.jeremiasAvero.app.category.application.CategoryService;
import com.jeremiasAvero.app.category.infraestructure.ports.in.dto.CategoryResponseDto;
import com.jeremiasAvero.app.category.infraestructure.ports.in.dto.CreateCategoryRequestDto;
import com.jeremiasAvero.app.category.infraestructure.ports.in.dto.UpdateCategoryRequestDto;
import com.jeremiasAvero.app.category.infraestructure.ports.in.mapper.CategoryWebMapper;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/categories")
public class CategoryController {
    private final CategoryService categoryService;
    private final CategoryWebMapper mapper;

    public CategoryController(
     CategoryService categoryService,
     CategoryWebMapper mapper
    ){
        this.categoryService = categoryService;
        this.mapper = mapper;
    }

    @GetMapping
    public ResponseEntity<List<CategoryResponseDto>> findAll(){
        var categories= categoryService.findAll();
        if (categories.isEmpty()) return ResponseEntity.noContent().build();
        CategoryResponseDto dto = new CategoryResponseDto();
        return ResponseEntity.ok(mapper.toResponseList(categories));
    }

    @PostMapping
    public ResponseEntity<?> create(
            @Valid @RequestBody CreateCategoryRequestDto req){
        var entity = mapper.toEntity(req);
        var saved = categoryService.save(entity);
        return ResponseEntity.status(201).body(mapper.toResponseDto(saved));
    }
    @PutMapping("/{id}")
    public ResponseEntity<CategoryResponseDto> update(
            @PathVariable Long id,
            @Valid @RequestBody UpdateCategoryRequestDto req) {
        var existing = categoryService.findById(id); // o 404 si no existe

        mapper.updateEntity(existing, req);
        var saved = categoryService.save(existing);
        return ResponseEntity.ok(mapper.toResponseDto(saved));
    }
}
