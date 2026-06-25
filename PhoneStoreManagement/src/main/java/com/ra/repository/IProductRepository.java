package com.ra.repository;

import com.ra.model.Product;

import java.util.List;

public interface IProductRepository {
    List<Product> findAll();

    Product findId(int id);

    boolean insert(Product product);
    boolean update(Product product);
    boolean delete(int id);
//    boolean updateStock(int productId, int newStock);

}
