package com.ra.service.impl;

import com.ra.exception.ForeignKeyException;
import com.ra.exception.NotFoundException;
import com.ra.exception.ValidationException;
import com.ra.model.Product;
import com.ra.repository.IInvoiceRepository;
import com.ra.repository.IProductRepository;
import com.ra.service.IProductService;

import java.util.List;
import java.util.stream.Collectors;

public class ProductService implements IProductService {

    private final IProductRepository productRepo;
    private final IInvoiceRepository invoiceRepo;

    // Khởi tạo implementation cụ thể ở constructor | Service nhận Repository từ bên ngoài
    public ProductService(IProductRepository productRepo,
                          IInvoiceRepository invoiceRepo){
        this.productRepo = productRepo;
        this.invoiceRepo = invoiceRepo;
    }


    @Override
    public List<Product> findAll() {
        return productRepo.findAll();
    }


    @Override
    public Product findById(int id) {
        Product product = productRepo.findById(id); //Validation
        if (product == null){
            throw NotFoundException.product(id); // dùng factory method
        }

        return product;
    }

    @Override
    public boolean save(Product product) {
        // Validation đầu vào trước khi lưu xuống DB
        if (product.getName() == null || product.getName().isBlank()){
            throw new ValidationException("Tên sản phẩm không được để trống!");
        }
        if (product.getPrice() < 0){
            throw new ValidationException("Giá sản phẩm không được âm!");
        }
        if (product.getStock() < 0){
            throw new ValidationException("Số lượng tồn kho không được âm!");
        }
        return productRepo.save(product);
    }

    @Override
    public boolean update(Product product) {

        findById(product.getId()); // kiểm tra sản phẩm tồn tại trước khi update

        if (product.getPrice() < 0){
            throw new ValidationException("Giá sản phẩm không được âm!");
        }

        return productRepo.update(product);
    }

    @Override
    public boolean delete(int id) {
        findById(id); // ném NotFoundException nếu không tìm thấy
        if (invoiceRepo.existsByProductId(id)) {
            throw ForeignKeyException.productHasInvoice();
        }

        return productRepo.delete(id);
    }

    // Tìm kiếm brand, gần đúng, không phân biệt hoa thường
    @Override
    public List<Product> searchByBrand(String brand) {
        String keyword = brand.toLowerCase().trim();

        return productRepo.findAll()
                .stream()
                .filter(p -> p.getBrand().toLowerCase().contains(keyword))
                .collect(Collectors.toList());
    }

    // Lọc theo khoảng giá
    @Override
    public List<Product> filterByPriceRange(double minPrice, double maxPrice) {
        if (minPrice > maxPrice) {
            throw new ValidationException("Giá tối thiểu không được lớn hơn giá tối đa!");
        }

        return productRepo.findAll()
                .stream()
                .filter(p -> p.getPrice() >= minPrice && p.getPrice() <= maxPrice)
                .collect(Collectors.toList());
    }

    // Tìm theo tên và chỉ lấy sản phẩm còn hàng (stock > 0)
    @Override
    public List<Product> searchByNameInStock(String name) {
        String keyword = name.toLowerCase().trim();

        return productRepo.findAll()
                .stream()
                .filter(p -> p.getName().toLowerCase().contains(keyword)
                        && p.getStock() > 0)
                .collect(Collectors.toList());
    }

}
