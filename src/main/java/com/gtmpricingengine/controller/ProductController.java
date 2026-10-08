package com.gtmpricingengine.controller;

import com.gtmpricingengine.dto.CreateProductRequest;
import com.gtmpricingengine.model.Product;
import com.gtmpricingengine.service.ProductService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/products")
public class ProductController {

    private final ProductService productService;

    public ProductController(
    ProductService productService
    ) {
        this.productService = productService;
    }


    @PostMapping
    public ResponseEntity<Product> createProduct(
    @Valid
    @RequestBody
    CreateProductRequest request
    ) {

        Product product =
        productService.createProduct(request);

        return ResponseEntity
            .status(HttpStatus.CREATED)
            .body(product);
    }


    @GetMapping("/{sku}")
    public ResponseEntity<Product> getProduct(
    @PathVariable String sku
    ) {

        Product product =
        productService.getBySku(sku);

        return ResponseEntity.ok(product);
    }
}