package com.gtmpricingengine.model;

import org.junit.jupiter.api.Test;
import com.gtmpricingengine.model.Product;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class ProductTest {
    @Test
    void shouldCalculateMarginCorrectly(){

        Product Product = new Product(
                "TEST-001",
                "TEST Product",
                100,
                60
        );

        double margin = Product.calculateMargin(100);

        assertEquals(0.4, margin, 0.0001);
    }
}
