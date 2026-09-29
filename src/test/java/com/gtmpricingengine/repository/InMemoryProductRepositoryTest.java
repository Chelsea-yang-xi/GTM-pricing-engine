package com.gtmpricingengine.repository;

class InMemoryProductRepositoryTest
        extends ProductRepositoryContractTest {

    @Override
    protected ProductRepository createRepository() {
        return new InMemoryProductRepository();
    }
}