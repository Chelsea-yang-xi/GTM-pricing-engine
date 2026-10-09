package com.gtmpricingengine.service;

import com.gtmpricingengine.dto.CreateProductRequest;
import com.gtmpricingengine.exception.ResourceNotFoundException;
import com.gtmpricingengine.model.Product;
import com.gtmpricingengine.repository.SpringDataProductRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ProductService {

    private final SpringDataProductRepository repository;


    public ProductService(
            SpringDataProductRepository repository
    ) {
        this.repository = repository;
    }


    @Transactional
    public Product createProduct(
            CreateProductRequest request
    ) {

        Product product =
                new Product(
                        request.sku(),
                        request.name(),
                        request.rrp(),
                        request.cost()
                );

        return repository.save(product);
    }


    @Transactional(readOnly = true) // this transaction only reads
    public Product getBySku(
            String sku
    ) {

        return repository
                .findById(sku)
                .orElseThrow(
                        () ->
                                new ResourceNotFoundException(
                                        "Product not found: "
                                                + sku
                                )
                );
    }
}