package com.ra.service.impl;

import com.ra.model.Product;
import com.ra.repository.IProductRepository;
import com.ra.repository.impl.ProductRepository;
import com.ra.service.IProductService;

import java.util.List;

public class ProductService implements IProductService {

    private final IProductRepository productRepo;

    public ProductService(){
        this.productRepo = new ProductRepository();
    }


    @Override
    public List<Product> findAll() {
        return productRepo.findAll();
    }

    @Override
    public Product findById(int id) {
        Product product = productRepo.findId(id);
        if (product == null){
            throw new RuntimeException("Không tìm thấy sản phẩm với ID " + id);
        }

        return product;
    }

    @Override
    public boolean save(Product product) {
        if (product.getName() == null || product.getName().isBlank()){
            throw new RuntimeException("Tên sản phẩm không được để trống!");
        }
        if (product.getPrice() < 0){
            throw new RuntimeException("Giá sản phẩm không được âm!");
        }
        if (product.getStock() < 0){
            throw new RuntimeException("Số lượng tồn kho không được âm!");
        }
        return productRepo.save(product);
    }

    @Override
    public boolean update(Product product) {

        findById(product.getId()); // kiểm tra sản phẩm tồn tại trước khi update

        if (product.getPrice() < 0){
            throw new RuntimeException("Giá sản phẩm không được âm!");
        }

        return productRepo.update(product);
    }

    @Override
    public boolean delete(int id) {

        findById(id); // kiểm tra sản phẩm tồn tại trước khi xóa
        return productRepo.delete(id);
    }
}
