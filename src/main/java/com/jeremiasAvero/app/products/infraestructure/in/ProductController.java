package com.jeremiasAvero.app.products.infraestructure.in;

import com.jeremiasAvero.app.products.application.ProductService;
import com.jeremiasAvero.app.products.infraestructure.in.mapper.ProductWebMapper;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/products")
public class ProductController {
    private final ProductService productService;
    private final ProductWebMapper mapper;

    public ProductController(
            ProductService productService,
        ProductWebMapper mapper
    ){
        
    }
}
