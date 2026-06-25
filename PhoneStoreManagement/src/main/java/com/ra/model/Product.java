package com.ra.model;

import java.math.BigDecimal;

public class Product {
    private int id;
    private String name;
    private String brand;
    private BigDecimal price;
    private int stock;

    public Product(){}

    // constructor có tham số
    public Product(String name, String  brand, BigDecimal price, int stock){
        this.name = name;
        this.brand = brand;
        this.price = price;
        this.stock = stock;
    }

    public Product(int id, String name, String  brand, BigDecimal price, int stock){
        this.id = id;
        this.name = name;
        this.brand = brand;
        this.price = price;
        this.stock = stock;
    }


    // getter và setter
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public int getStock() {
        return stock;
    }

    public void setStock(int stock) {
        this.stock = stock;
    }

    @Override
    public String toString() {
        return "Product{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", brand='" + brand + '\'' +
                ", price=" + price +
                ", stock=" + stock +
                '}';
    }
}
