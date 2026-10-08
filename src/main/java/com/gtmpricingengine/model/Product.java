package com.gtmpricingengine.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity  // this class maps to database table
@Table(name = "products")
public class Product {

    @Id  // sku is primary key
    private String sku;

    private String name;

    private double rrp;

    private double cost;


    protected Product() {
        // Required by JPA
    }


    public Product(
            String sku,
            String name,
            double rrp,
            double cost
    ) {
        this.sku = sku;
        this.name = name;
        this.rrp = rrp;
        this.cost = cost;
    }


    public String getSku() {
        return sku;
    }


    public String getName() {
        return name;
    }


    public double getRrp() {
        return rrp;
    }


    public double getCost() {
        return cost;
    }


    public double calculateMargin(
            double sellingPrice
    ) {
        return (
                sellingPrice - cost
        ) / sellingPrice;
    }
}