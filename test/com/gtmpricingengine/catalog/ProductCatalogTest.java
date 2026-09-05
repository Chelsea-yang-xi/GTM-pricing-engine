package com.gtmpricingengine.catalog;

import com.gtmpricingengine.model.Product;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class ProductCatalogTest {
    @Test
    void shouldAddAndFindProductBySku() {
        ProductCatalog catalog = new ProductCatalog();
        Product product = new Product(
                "A2148", "Anker Powercore", 59.99, 30.0
        );
        catalog.addProduct(product);
        Product result =
                catalog.getProduct("A2148");
        assertEquals(product, result);
        assertEquals(1, catalog.size());
    }

    @Test
    void shouldThrowExceptionWhenProductNotFound() {

        ProductCatalog catalog =
                new ProductCatalog();

        IllegalArgumentException exception =
                assertThrows(
                        IllegalArgumentException.class,
                        () -> catalog.getProduct("NON-EXISTENT")
                );

        assertEquals(
                "Product not found: NON-EXISTENT",
                exception.getMessage()
        );
    }
}
