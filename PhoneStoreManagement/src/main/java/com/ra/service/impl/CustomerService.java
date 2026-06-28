package com.ra.service.impl;

import com.ra.config.DBConnection;
import com.ra.model.Customer;
import com.ra.repository.ICustomerRepository;
import com.ra.repository.impl.CustomerRepository;
import com.ra.service.ICustomerService;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.ArrayList;
import java.util.List;

public class CustomerService implements ICustomerService {

    private final ICustomerRepository customerRepo;

    public CustomerService(ICustomerRepository customerRepo){
        this.customerRepo = customerRepo;
    }



    @Override
    public List<Customer> findAll() {
        return customerRepo.findAll();
    }

    @Override
    public Customer findById(int id) {
        Customer customer = customerRepo.findById(id);
        if (customer == null) {
            throw new RuntimeException("Không tìm thấy khách hàng với ID: " + id);
        }
        return customer;
    }

    @Override
    public Customer findByEmail(String email) {
        Customer customer = customerRepo.findByEmail(email);
        if (customer == null) {
            throw new RuntimeException("Không tìm thấy khách hàng với email " + email);
        }
        return customerRepo.findByEmail(email);
    }

    @Override
    public boolean save(Customer customer) {
        if (customer.getName() == null || customer.getName().isBlank()){
            throw new RuntimeException("Tên khách hàng không được để trống!");
        }
        if (customer.getEmail() == null || customer.getEmail().isBlank()){
            throw new RuntimeException("Email khách hàng không được để trống!");
        }
        if (customerRepo.findByEmail(customer.getEmail()) != null) {
            throw new RuntimeException("Email đã tồn tại trong hệ thống");
        }
        return customerRepo.save(customer);
    }

    @Override
    public boolean update(Customer customer) {
        findById(customer.getId()); // kiểm tra khách hàng tồn tại trước khi update
        return customerRepo.update(customer);
    }

    @Override
    public boolean delete(int id) {
        findById(id);
        return customerRepo.delete(id);
    }
}
