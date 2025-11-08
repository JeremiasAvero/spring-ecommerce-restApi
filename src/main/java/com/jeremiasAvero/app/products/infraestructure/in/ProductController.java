package com.jeremiasAvero.app.products.infraestructure.in;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jeremiasAvero.app.brand.application.BrandService;
import com.jeremiasAvero.app.category.application.CategoryService;
import com.jeremiasAvero.app.image.application.StorageService;
import com.jeremiasAvero.app.image.domain.ImageEntity;
import com.jeremiasAvero.app.image.infraestructure.ImageMeta;
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
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@RestController
@RequestMapping("api/products")
public class ProductController {
    private final ProductService productService;
    private final CategoryService categoryService;
    private final BrandService brandService;
    private final ProductWebMapper mapper;
    private final StorageService storage;
    private final ObjectMapper om;
    public ProductController(
            ProductService productService,
            ProductWebMapper mapper,
            CategoryService categoryService,
            BrandService brandService,
            StorageService storage,
            ObjectMapper om
    ){
        this.productService = productService;
        this.categoryService = categoryService;
        this.brandService = brandService;
        this.mapper = mapper;
        this.storage = storage;
        this.om = om;
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

    @PostMapping(
            value = "/with-images",
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE
    )
    public ResponseEntity<ProductResponseDto> createWithImages(
            @RequestPart("product") @Valid CreateProductRequestDto productReq,
            @RequestPart(value = "images", required = false) List<MultipartFile> images,
            @RequestPart(value = "metas", required = false) String metasJson
    ) throws Exception {

        var category = categoryService.findById(productReq.getCategoryId().longValue());
        var brand = brandService.findById(productReq.getBrandId().longValue());

        var prod = mapper.toEntity(productReq, category, brand);
        prod = productService.save(prod);

        List<ImageMeta> metas = Collections.emptyList();
        if (metasJson != null && !metasJson.isBlank()) {
            metas = om.readValue(metasJson, new TypeReference<List<ImageMeta>>() {});
        }

        var uploadedKeys = new ArrayList<String>();
        try {
            if (images != null) {
                for (int i = 0; i < images.size(); i++) {
                    MultipartFile file = images.get(i);
                    ImageMeta meta = (metas.size() > i) ? metas.get(i) : new ImageMeta(null, i, i == 0);

                    String url = storage.uploadProductImage(prod.getId(), file);

                    var img = new ImageEntity();
                    img.setProduct(prod);
                    img.setImageUrl(url);
                    img.setAltText(meta.alt() != null ? meta.alt() : prod.getName());
                    img.setPosition(meta.position() != null ? meta.position() : i);
                    img.setPrimaryImage(Boolean.TRUE.equals(meta.primary()));

                    prod.addImage(img);
                }
            }

            prod = productService.save(prod);

        } catch (Exception ex) {
            throw ex;
        }

        return ResponseEntity.status(201).body(mapper.toResponseDto(prod));
    }

}
