package com.ra.service.impl;

import com.ra.model.Product;
import com.ra.repository.IProductRepository;
import com.ra.service.IProductService;

import java.util.List;
import java.util.stream.Collectors;

public class ProductService implements IProductService {

    private final IProductRepository productRepo;

    public ProductService(IProductRepository productRepo){
        this.productRepo = productRepo;
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

    @Override
    public List<Product> searchByNameOrBrand(String keyword) {
        String lowerKeyWord = keyword.toLowerCase().trim();

        return productRepo.findAll()
                .stream()
                .filter(p -> p.getName().toLowerCase().contains(lowerKeyWord) ||
                        p.getBrand().toLowerCase().contains(lowerKeyWord))
                .collect(Collectors.toList());
    }

    @Override
    public List<Product> filterByPriceRange(double minPrice, double maxPrice) {
        if (minPrice > maxPrice) {
            throw new RuntimeException("Giá tối thiểu không được lớn hơn giá tối đa!");
        }

        return productRepo.findAll()
                .stream()
                .filter(p -> p.getPrice() >= minPrice && p.getPrice() <= maxPrice)
                .collect(Collectors.toList());
    }

}
