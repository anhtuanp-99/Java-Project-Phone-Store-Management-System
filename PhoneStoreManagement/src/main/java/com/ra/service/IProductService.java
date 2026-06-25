package com.ra.service;

import com.ra.model.Product;

import java.util.List;

public interface IProductService {
    // Lấy toàn bộ danh sách sản phẩm
    List<Product> findAll();

    // Tìm sản phẩm theo ID
    Product findId(int id);

    // Thêm sản phẩm mới
    boolean save(Product product);

    // Cập nhật sản phẩm
    boolean update(Product product);

    // Xóa sản phẩm
    boolean delete(int id);
}
