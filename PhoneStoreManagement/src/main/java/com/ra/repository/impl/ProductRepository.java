package com.ra.repository.impl;

import com.ra.config.DBConnection;
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
        String sql = "SELECT id, name, brand, price, stock FROM product ORDER BY id";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

             while (rs.next()){
                 Product p = new Product();
                 p.setId(rs.getInt("id")) ;
                 p.setName(rs.getString("customer_name"));
                 p.setBrand(rs.getString("brand"));
                 p.setStock(rs.getInt("stock"));
                 products.add(p);
             }

        } catch (SQLException e){
            System.out.println("Lỗi khi lấy danh sách sản phẩm: " + e.getMessage());
        }
        return products;
    }


    @Override
    public Product findId(int id) {
        String sql = "SELECT id, name, brand, price, stock FROM product WHERE id = ?" ;
        try (Connection conn = DBConnection.getConnection();
            PreparedStatement stmt = conn.prepareStatement(sql)){
            stmt.setInt(1, id);
            try (ResultSet rs = stmt.executeQuery()){
                if (rs.next()){
                    Product p = new Product();
                    p.setId(rs.getInt("id"));
                    p.setName(rs.getString("name"));
                    p.setBrand(rs.getString("brand"));
                    p.setPrice(rs.getBigDecimal("price"));
                    p.setStock(rs.getInt("stock"));
                    return p;
                }
            }
        } catch (SQLException e) {
            System.out.println("Lỗi khi tìm sản phẩm ID " + id + ": " + e.getMessage());
        }
        return null;
    }

    @Override
    public boolean save(Product product) {
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
        String sql = "UPDATE product SET name=?, brand=?, price=?, stock=? WHERE id=?";
        try (Connection conn = DBConnection.getConnection();
            PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, product.getName());
            stmt.setString(2, product.getBrand());
            stmt.setBigDecimal(3, product.getPrice());
            stmt.setInt(4, product.getStock());
            stmt.setInt(5, product.getId());

            return stmt.executeUpdate() > 0;
        } catch (SQLException e){
            System.out.println("Lỗi không thể cập nhật sản phẩm: " + e.getMessage());
        }
        return false;
    }

    @Override
    public boolean delete(int id) {
        String sql = "DELETE FROM product WHERE id = ?";
        try (Connection conn = DBConnection.getConnection();
            PreparedStatement stmt = conn.prepareStatement(sql)){

            stmt.setInt(1, id);
            return stmt.executeUpdate() > 0;

        } catch (SQLException e){
            System.out.println("Không thể xóa bảng có ID " + id + ": " + e.getMessage());
        }
        return false;
    }
}
