package com.ra.repository;

import com.ra.model.Product;

import java.sql.Connection;
import java.util.List;

// Interface định nghĩa các thao với bảng PRODUCT trong database

public interface IProductRepository {

    // Lấy toàn bộ danh sách sản phẩm
    List<Product> findAll();

    // Tìm sản phẩm theo ID
    Product findById(int id);

    // Thêm sản phẩm mới
    boolean save(Product product);

    // Cập nhật sản phẩm
    boolean update(Product product);

    // Xóa sản phẩm
    boolean delete(int id);

    // Cập nhật riêng số lượng tồn kho (dùng khi tạo hóa đơn)
    boolean updateStock(int productId, int newStock);

    boolean updateStockWithConnection(int productId, int newStock, Connection conn);

    Product findByIdWithConnection(int id, Connection conn);
}
