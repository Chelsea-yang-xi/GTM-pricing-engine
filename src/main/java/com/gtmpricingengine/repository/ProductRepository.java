package com.gtmpricingengine.repository;
import com.gtmpricingengine.model.Product;
import java.util.Collection;

//Dependency Inversion Principle--just an interface!
//function design: save product/ find product by sku/ find all products in collection/ check if a sku exists/ counts

public interface ProductRepository {
    void save(Product product);
    Product findBySku(String sku);
    Collection<Product> findAll();
    boolean existsBySku(String sku);
    int count();
}
