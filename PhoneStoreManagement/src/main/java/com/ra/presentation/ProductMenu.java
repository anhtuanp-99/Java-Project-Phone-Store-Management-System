package com.ra.presentation;

import com.ra.exception.ForeignKeyException;
import com.ra.exception.NotFoundException;
import com.ra.model.Product;
import com.ra.repository.IInvoiceRepository;
import com.ra.repository.IProductRepository;
import com.ra.repository.impl.InvoiceRepository;
import com.ra.repository.impl.ProductRepository;
import com.ra.service.IProductService;
import com.ra.service.impl.ProductService;
import com.ra.utils.InputUtils;

import java.util.List;

public class ProductMenu {

    private final IProductService productService; // Dependency Injection

    // Presentation tạo dependency chain
    public ProductMenu() {
        IProductRepository productRepo = new ProductRepository();
        IInvoiceRepository invoiceRepo = new InvoiceRepository();
        this.productService = new ProductService(productRepo, invoiceRepo);
    }

    public void show() {
        while (true) {
            System.out.println("\n--- Quản lí sản phẩm ---");
            System.out.println("[1] Xem danh sách");
            System.out.println("[2] Thêm sản phẩm");
            System.out.println("[3] Chỉnh sửa sản phẩm");
            System.out.println("[4] Xóa sản phẩm");
            System.out.println("[5] Tìm kiếm theo Brand");
            System.out.println("[6] Lọc theo khoảng giá");
            System.out.println("[7] Tìm kiếm theo tên + còn hàng");
            System.out.println("[0] Quay lại");

            int choice = InputUtils.getIntRange("Chọn: ", 0, 7);

            switch (choice) {
                case 1 -> showAll();
                case 2 -> addProduct();
                case 3 -> updateProduct();
                case 4 -> deleteProduct();
                case 5 -> searchByBrand();
                case 6 -> filterByPrice();
                case 7 -> searchByNameInStock();
                case 0 -> { return; }
            }
        }
    }


    private void showAll() {
        List<Product> list = productService.findAll();
        if (list.isEmpty()) {
            System.out.println("Chưa có sản phẩm nào");
            return;
        }
        list.forEach(System.out::println);
        System.out.println("Tổng: " + list.size() + " sản phẩm");
    }

    private void addProduct() {
        System.out.println("\n--- Thêm sản phẩm mới ---");
        Product product = new Product();
        product.setName(InputUtils.getString("Tên sản phẩm: "));
        product.setBrand(InputUtils.getString("Hãng sản phẩm: "));
        product.setPrice(InputUtils.getDouble("Giá bán (USD): "));
        product.setStock(InputUtils.getInt("Số lượng tồn kho: "));
        try {
            if (productService.save(product)) {
                System.out.println("Thêm sản phẩm thành công");
            }
        } catch (RuntimeException e) {
            System.out.println("Lỗi: " + e.getMessage());
        }

    }

    private void updateProduct() {
        System.out.println("\n--- Chỉnh sửa sản phẩm ---");
        int id = InputUtils.getInt("Nhập ID sản phẩm cần sửa: ");
        try {
            Product product = productService.findById(id);
            System.out.println();
            System.out.println("Thông tin hiện tại: ");
            System.out.println(product);
            System.out.println("\n-- Chọn thuộc tính cần sửa --");
            System.out.println("[1]. Tên sản phẩm");
            System.out.println("[2]. Hãng sản phẩm");
            System.out.println("[3]. Giá bán");
            System.out.println("[4]. Tồn kho");
            System.out.println("[0]. Hủy");

            int choice = InputUtils.getIntRange("Chọn", 0, 4);
            switch (choice) {
                case 1 -> product.setName(InputUtils.getOptionalString("Tên mới: "));
                case 2 -> product.setBrand(InputUtils.getOptionalString("Hãng mới: "));
                case 3 -> product.setPrice(InputUtils.getDouble("Giá mới (USD): "));
                case 4 -> product.setStock(InputUtils.getInt("Tồn kho mới: "));
                case 0 -> { return; }
            }

            if (productService.update(product)) {
                System.out.println("Cập nhật thành công!");
            }

        } catch (RuntimeException e) {
            System.out.println("Lỗi: " + e.getMessage());
        }
    }

    private void deleteProduct() {
        System.out.println("\n--- Xóa sản phẩm ---");
        int id = InputUtils.getInt("Nhập id sản phẩm cần xóa: ");

        try {
            Product  product = productService.findById(id);
            System.out.println("Sản phẩm tìm thấy: ");
            System.out.println(product);

            if (InputUtils.getConfirmation("Bạn có chắc chắn muốn xóa sản phẩm này?")) {
                if (productService.delete(id)) {
                    System.out.println(" Đã xóa sản phẩm");
                }
            } else {
                System.out.println("Đã hủy thao tác xóa");
            }

        } catch (NotFoundException e) {
            // ID không tồn tại
            System.out.println("-> " + e.getMessage());
        } catch (ForeignKeyException e) {
            // Sản phẩm đang có trong hóa đơn
            System.out.println("-> " + e.getMessage());
        } catch (RuntimeException e) {
            // Các lỗi không xác định
            System.out.println("-> " + e.getMessage());
        }
    }

    private void searchByBrand() {
        System.out.println("\n--- Tìm điện thoại theo Brand ---");
        String brand = InputUtils.getString("Nhập tên hãng cần tìm: ");

        List<Product> results = productService.searchByBrand(brand);

        if (results.isEmpty()) {
            System.out.println("Không tìm thấy sản phẩm liên quan đến hãng '" + brand + "'");
        } else {
            System.out.println("\nKết quả tìm kiếm (" + results.size() + " sản phẩm): ");
            results.forEach(System.out::println);
        }

    }

    private void filterByPrice() {
        System.out.println("\n --- Tìm kiếm điện thoại theo khoảng giá cố định ---");
        double minPrice = InputUtils.getDouble("Giá tối thiểu (USD): ");
        double maxPrice = InputUtils.getDouble("Giá tối đa (USD): ");

        try {
            List<Product> results = productService.filterByPriceRange(minPrice, maxPrice);
            if (results.isEmpty()) {
                System.out.println("Không có sản phẩm nào trong khoảng giá này!");
            } else {
                System.out.println("\nKết quả tìm kiếm (" + results.size() + " sản phẩm): ");
                results.forEach(System.out::println);
            }
        } catch (RuntimeException e) {
            System.err.println("Lỗi: " + e.getMessage());
        }
    }

    private void searchByNameInStock() {
        System.out.println("\n--- Tìm điện thoại theo tên ---");
        String name = InputUtils.getString("Nhập tên điện thoại cần tìm: ");

        List<Product> results = productService.searchByNameInStock(name);

        if (results.isEmpty()) {
            System.out.println("Không tìm thấy sản phẩm liên quan đến '" + name + "'");
        } else {
            System.out.println("\nKết quả tìm kiếm (" + results.size() + " sản phẩm): ");
            results.forEach(System.out::println);
        }
    }
}
