package com.ra.repository;

import com.ra.model.Customer;
import com.ra.model.Product;

import java.util.List;

public interface ICustomerRepository {
    //Lấy toàn bộ danh sách khách hàng
    List<Customer> findAll();

    // Tìm theo ID
    Customer findById(int id);

    // Thêm khách hàng mới
    boolean insert(Customer customer);

    // Cập nhật thông tin khách hàng
    boolean update(Customer customer);

    // Xóa khách hàng theo ID
    boolean delete(int id);
}
