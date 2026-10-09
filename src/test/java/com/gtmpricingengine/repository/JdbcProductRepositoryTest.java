package com.gtmpricingengine.repository;

import java.util.UUID;

class JdbcProductRepositoryTest
        extends ProductRepositoryContractTest {

    @Override
    protected ProductRepository createRepository() {

        String databaseName =
                "test_" +
                        UUID.randomUUID()
                                .toString()
                                .replace("-", "");

        DatabaseManager databaseManager =
                new DatabaseManager(
                        "jdbc:h2:mem:" +
                                databaseName +
                                ";DB_CLOSE_DELAY=-1"
                );

        databaseManager.initializeDatabase();

        return new JdbcProductRepository(
                databaseManager
        );
    }
}