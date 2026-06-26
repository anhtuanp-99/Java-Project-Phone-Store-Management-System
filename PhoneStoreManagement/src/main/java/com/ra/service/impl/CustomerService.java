package com.ra.service.impl;

import com.ra.model.Customer;
import com.ra.service.ICustomerService;

import java.util.List;

public class CustomerService implements ICustomerService {



    @Override
    public List<Customer> findAll() {

        return List.of();
    }

    @Override
    public Customer findById(int id) {
        return null;
    }

    @Override
    public Customer findByEmail(String email) {
        return null;
    }

    @Override
    public boolean save(Customer customer) {
        return false;
    }

    @Override
    public boolean update(Customer customer) {
        return false;
    }

    @Override
    public boolean delete(int id) {
        return false;
    }
}
