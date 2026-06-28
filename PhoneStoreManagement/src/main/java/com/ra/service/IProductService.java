package com.ra.service;

import com.ra.model.Product;

import java.util.List;

public interface IProductService {
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

//    // Tìm kiếm theo tên hoặc theo hãng
//    List<Product> searchByNameOrBrand(String keyword);
//
//    // Lọc theo khoảng giá
//    List<Product> filterByPriceRange(double minPrice, double maxPrice);
}
