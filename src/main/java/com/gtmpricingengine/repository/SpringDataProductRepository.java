package com.gtmpricingengine.repository;

import com.gtmpricingengine.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SpringDataProductRepository
        extends JpaRepository<Product, String> { // entity type = product; primary key type = string
}