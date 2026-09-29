package com.gtmpricingengine.repository;
import com.gtmpricingengine.model.Product;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

//inmemory: can be used for test, java collectio storage; inSKU as key & Product as value
public class InMemoryProductRepository implements ProductRepository {
    private final Map<String, Product> products = new HashMap<>();

    // save product
    @Override
    public void save(Product product){
        products.put(product.getSku(), product);
    }

    // find product by SKU if it cannot find then raise exception
    @Override
    public Product findBySku(String sku){
        Product product = products.get(sku);
        if(product == null){
            throw new IllegalArgumentException("Product not found: " + sku);
        }
        return product;
    }

    // find all products
    @Override
    public Collection<Product> findAll() {
        return new ArrayList<>(products.values());
    }

    @Override
    public boolean existsBySku(String sku){
        return products.containsKey(sku);
    }

    @Override
    public int count(){
        return products.size();
    }
}