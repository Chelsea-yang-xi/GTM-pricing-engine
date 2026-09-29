package com.gtmpricingengine.repository;

import com.gtmpricingengine.model.Product;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import com.gtmpricingengine.repository.ProductRepository;

import static org.junit.jupiter.api.Assertions.*;

abstract class ProductRepositoryContractTest {

    protected ProductRepository repository;

    protected abstract ProductRepository createRepository();

    @BeforeEach
    void setUp() {
        repository = createRepository();
    }
    @Test
    void shouldSaveAndFindProductBySku() {

        Product product =
                new Product(
                        "TEST001",
                        "Test Product",
                        100.0,
                        60.0
                );

        repository.save(product);

        Product result =
                repository.findBySku("TEST001");

        assertEquals("TEST001", result.getSku());
        assertEquals("Test Product", result.getName());
        assertEquals(100.0, result.getRrp(), 0.001);
        assertEquals(60.0, result.getCost(), 0.001);
    }

    @Test
    void shouldOverwriteProductWhenSkuAlreadyExists() {

        Product firstProduct =
                new Product(
                        "TEST001",
                        "Test Product",
                        100.0,
                        60.0
                );

        Product updatedProduct =
                new Product(
                        "TEST001",
                        "Updated Test Product",
                        120.0,
                        70.0
                );

        repository.save(firstProduct);
        repository.save(updatedProduct);

        Product result =
                repository.findBySku("TEST001");

        assertEquals(1, repository.count());
        assertEquals(
                "Updated Test Product",
                result.getName()
        );
        assertEquals(
                120.0,
                result.getRrp(),
                0.001
        );
    }

    @Test
    void shouldThrowExceptionWhenProductNotFound() {

        IllegalArgumentException exception =
                assertThrows(
                        IllegalArgumentException.class,
                        () ->
                                repository.findBySku(
                                        "NON-EXISTENT"
                                )
                );

        assertEquals(
                "Product not found: NON-EXISTENT",
                exception.getMessage()
        );
    }

    @Test
    void shouldReportWhetherSkuExists() {

        Product product =
                new Product(
                        "TEST001",
                        "Test Product",
                        100.0,
                        60.0
                );

        repository.save(product);

        assertTrue(
                repository.existsBySku("TEST001")
        );

        assertFalse(
                repository.existsBySku("MISSING")
        );
    }

    @Test
    void shouldFindAllProducts() {

        repository.save(
                new Product(
                        "A001",
                        "Product A",
                        100.0,
                        60.0
                )
        );

        repository.save(
                new Product(
                        "B001",
                        "Product B",
                        150.0,
                        80.0
                )
        );

        assertEquals(
                2,
                repository.findAll().size()
        );
    }
}