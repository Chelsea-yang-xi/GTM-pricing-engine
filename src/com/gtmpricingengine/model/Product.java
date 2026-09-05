package com.gtmpricingengine.model;

public class Product {
    private String sku;
    private String name;
    private double rrp;
    private double cost;

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

    public String getSku(){
        return sku;
    }
    public String getName(){
        return name;
    }
    public double getRrp(){
        return rrp;
    }
    public double getCost(){
        return cost;
    }
    public double calculateMargin(double sellingPrice) {
        return (sellingPrice - cost)/ sellingPrice;
    }
}
