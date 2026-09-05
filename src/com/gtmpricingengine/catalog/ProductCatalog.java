package com.gtmpricingengine.catalog;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import com.gtmpricingengine.model.Product;

public class ProductCatalog {

    private final Map<String, Product> products =
            new HashMap<>();

    public void addProduct(Product product) {
        products.put(
                product.getSku(),
                product
        );
    }

    public Product getProduct(String sku) {

        Product product =
                products.get(sku);

        if (product == null) {
            throw new IllegalArgumentException(
                    "Product not found: " + sku
            );
        }

        return product;
    }

    public boolean containsProduct(String sku) {
        return products.containsKey(sku);
    }

    public Collection<Product> getAllProducts() {
        return products.values();
    }

    public int size() {
        return products.size();
    }
}