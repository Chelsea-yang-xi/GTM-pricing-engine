package com.gtmpricingengine.repository;

import com.gtmpricingengine.model.Product;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

//connect to database using sql
public class JdbcProductRepository
        implements ProductRepository {

    private final DatabaseManager databaseManager;

    public JdbcProductRepository(
            DatabaseManager databaseManager
    ) {
        this.databaseManager =
                databaseManager;
    }

    //save products using sql with ? as placeholder, if the sku exists then put; if not then post
    @Override
    public void save(Product product) {

        String sql = """
                MERGE INTO products
                (sku, name, rrp, cost)
                KEY(sku)
                VALUES (?, ?, ?, ?)
                """;

        try (
                Connection connection =
                        databaseManager.getConnection();

                PreparedStatement statement =
                        connection.prepareStatement(sql)
        ) {

            statement.setString(
                    1,
                    product.getSku()
            );

            statement.setString(
                    2,
                    product.getName()
            );

            statement.setDouble(
                    3,
                    product.getRrp()
            );

            statement.setDouble(
                    4,
                    product.getCost()
            );

            statement.executeUpdate();

        } catch (SQLException e) {

            throw new RuntimeException(
                    "Failed to save product",
                    e
            );
        }
    }

    //select sku from the database, if not exists: throw exception
    @Override
    public Product findBySku(String sku) {

        String sql = """
                SELECT sku, name, rrp, cost
                FROM products
                WHERE sku = ?
                """;

        try (
                Connection connection =
                        databaseManager.getConnection();

                PreparedStatement statement =
                        connection.prepareStatement(sql)
        ) {

            statement.setString(1, sku);

            try (
                    ResultSet resultSet =
                            statement.executeQuery()
            ) {

                if (!resultSet.next()) {
                    throw new IllegalArgumentException(
                            "Product not found: " + sku
                    );
                }

                return new Product(
                        resultSet.getString("sku"),
                        resultSet.getString("name"),
                        resultSet.getDouble("rrp"),
                        resultSet.getDouble("cost")
                );
            }

        } catch (SQLException e) {

            throw new RuntimeException(
                    "Failed to find product",
                    e
            );
        }
    }

    //check if product exists by sku
    @Override
    public boolean existsBySku(String sku) {

        String sql = """
            SELECT COUNT(*)
            FROM products
            WHERE sku = ?
            """;

        try (
                Connection connection =
                        databaseManager.getConnection();

                PreparedStatement statement =
                        connection.prepareStatement(sql)
        ) {

            statement.setString(1, sku);

            try (ResultSet resultSet = statement.executeQuery()) {

                resultSet.next();

                return resultSet.getInt(1) > 0;
            }

        } catch (SQLException e) {

            throw new RuntimeException(
                    "Failed to check product existence",
                    e
            );
        }
    }
    //count
    @Override
    public int count() {

        String sql =
                "SELECT COUNT(*) FROM products";

        try (
                Connection connection =
                        databaseManager.getConnection();

                PreparedStatement statement =
                        connection.prepareStatement(sql);

                ResultSet resultSet =
                        statement.executeQuery()
        ) {

            resultSet.next();

            return resultSet.getInt(1);

        } catch (SQLException e) {

            throw new RuntimeException(e);
        }
    }

    //find all and add to arraylist
    @Override
    public Collection<Product> findAll() {

        List<Product> products =
                new ArrayList<>();

        String sql = """
                SELECT sku, name, rrp, cost
                FROM products
                """;

        try (
                Connection connection =
                        databaseManager.getConnection();

                PreparedStatement statement =
                        connection.prepareStatement(sql);

                ResultSet resultSet =
                        statement.executeQuery()
        ) {

            while (resultSet.next()) {

                Product product =
                        new Product(
                                resultSet.getString("sku"),
                                resultSet.getString("name"),
                                resultSet.getDouble("rrp"),
                                resultSet.getDouble("cost")
                        );

                products.add(product);
            }

            return products;

        } catch (SQLException e) {

            throw new RuntimeException(e);
        }
    }
}
