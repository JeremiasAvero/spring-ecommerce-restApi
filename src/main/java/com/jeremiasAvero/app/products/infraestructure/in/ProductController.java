package com.jeremiasAvero.app.products.infraestructure.in;

import com.jeremiasAvero.app.brand.application.BrandService;
import com.jeremiasAvero.app.category.application.CategoryService;
import com.jeremiasAvero.app.products.application.ProductService;
import com.jeremiasAvero.app.products.domain.ProductEntity;
import com.jeremiasAvero.app.products.infraestructure.in.dto.CreateProductRequestDto;
import com.jeremiasAvero.app.products.infraestructure.in.dto.ProductResponseDto;
import com.jeremiasAvero.app.products.infraestructure.in.dto.UpdateProductRequestDto;
import com.jeremiasAvero.app.products.infraestructure.in.mapper.ProductWebMapper;
import jakarta.validation.Valid;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/products")
public class ProductController {
    private final ProductService productService;
    private final CategoryService categoryService;
    private final BrandService brandService;
    private final ProductWebMapper mapper;

    public ProductController(
            ProductService productService,
            ProductWebMapper mapper,
            CategoryService categoryService,
            BrandService brandService
    ){
        this.productService = productService;
        this.categoryService = categoryService;
        this.brandService = brandService;
        this.mapper = mapper;
    }

    @GetMapping("/all")
    public ResponseEntity<List<ProductResponseDto>> findAll() {
        var products = productService.findAll();
        if (products.isEmpty()) return ResponseEntity.noContent().build();
        return ResponseEntity.ok(mapper.toResponseList(products));
    }

    
    @GetMapping()
    public ResponseEntity<Page<ProductResponseDto>> findAllPageable(@PageableDefault 
    		(size = 20, sort = "name", direction = Sort.Direction.ASC) Pageable pageable){
    
//    	if (( products).isEmpty()) return ResponseEntity.noContent().build();
    	  Page<ProductEntity> page = productService.findAllPageable(pageable);
    	  return ResponseEntity.ok(mapper.toResponsePage(page)); 
    }

    @PostMapping
    public ResponseEntity<ProductResponseDto> create(@Valid @RequestBody CreateProductRequestDto req){
        var category = categoryService.findById(Long.valueOf(req.getCategoryId()));
        var brand = brandService.findById(Long.valueOf(req.getBrandId()));

        var productEntity = mapper.toEntity(req, category, brand);
        var productSaved = productService.save(productEntity);
        return ResponseEntity.status(201).body(mapper.toResponseDto(productSaved));
    }
    @PutMapping("/{id}")
    public ResponseEntity<ProductResponseDto> update(@PathVariable Long id,
                                                     @Valid @RequestBody UpdateProductRequestDto req) throws Exception {
        var existing = productService.findById(id); // o 404 si no existe
        var category = categoryService.findById(req.getCategoryId());
        var brand = brandService.findById(req.getBrandId());

        mapper.updateEntity(existing, req, category, brand);
        var saved = productService.save(existing);
        return ResponseEntity.ok(mapper.toResponseDto(saved));
    }
}
