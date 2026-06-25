package com.ra.service;

import com.ra.model.Customer;

import java.util.List;

public interface ICustomerService {
    //Lấy toàn bộ danh sách khách hàng
    List<Customer> findAll();

    // Tìm theo ID
    Customer findById(int id);

    // Tìm theo email - dùng khi đăng nhập hoặc kiểm tra email trùng
    Customer findByEmail(String email);

    // Thêm khách hàng mới
    boolean save(Customer customer);

    // Cập nhật thông tin khách hàng
    boolean update(Customer customer);

    // Xóa khách hàng theo ID
    boolean delete(int id);
}
