package com.ra.repository.impl;

import com.ra.config.DBConnection;
import com.ra.model.Invoice;
import com.ra.model.Product;
import com.ra.repository.IProductRepository;

import java.math.BigDecimal;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ProductRepository implements IProductRepository {

    @Override
    public List<Product> findAll() {
        List<Product> products = new ArrayList<>();
        String sql = """
                SELECT i.id, i.customer_id, i.total_amount, c.name AS customer_name,
                       i.created_at, i.total_amount
                FROM invoice i
                JOIN customer c ON i.customer_id = c.id
                ORDER BY i.created_at DESC 
                """;
        try (Connection conn = DBConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

             while (rs.next()){
                 Invoice inv = new Invoice();
                 products.add();
             }

        } catch (SQLException e){
            e.printStackTrace();
        }
        return products;
    }


    @Override
    public Product findId(int id) {

        return null;
    }

    @Override
    public boolean insert(Product product) {
        String sql = "INSERT INTO product (name, brand, price, stock) VALUES (?, ?, ?, ?)";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

             stmt.setString(1, product.getName());
             stmt.setString(2, product.getBrand());
             stmt.setBigDecimal(3, product.getPrice());
             stmt.setInt(4, product.getStock());

             return stmt.executeUpdate() > 0; // trả về số dòng bị ảnh hưởng > 0
        } catch (SQLException e){
            System.out.println("Lỗi khi thêm sản phẩm: " + e.getMessage());
            return false;
        }
    }

    @Override
    public boolean update(Product product) {
        return false;
    }

    @Override
    public boolean delete(int id) {
        return false;
    }
}
