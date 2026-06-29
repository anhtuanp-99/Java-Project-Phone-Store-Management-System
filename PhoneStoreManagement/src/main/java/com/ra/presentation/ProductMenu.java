package com.ra.presentation;

import com.ra.model.Product;
import com.ra.service.IProductService;
import com.ra.service.impl.ProductService;
import com.ra.utils.InputUtils;

import java.util.List;

public class ProductMenu {

    private final IProductService productService;

    public ProductMenu() {
        this.productService = new ProductService();
    }

    public void show() {
        while (true) {
            System.out.println("\n--- Quản lí sản phẩm ---");
            System.out.println("1. Xem danh sách");
            System.out.println("2. Thêm sản phẩm");
            System.out.println("3. Chỉnh sửa sản phẩm");
            System.out.println("4. Xóa sản phẩm");
            System.out.println("5. Tìm kiếm theo tên/hãng");
            System.out.println("6. Lọc theo khoảng giá");
            System.out.println("0. Quay lại Menu chính");

            int choice = InputUtils.getIntRange("Chọn: ", 0, 6);

            switch (choice) {
                case 1 -> showAll();
                case 2 -> addProduct();
                case 3 -> updateProduct();
                case 4 -> deleteProduct();
                case 5 -> searchProduct();
                case 6 -> filterByPrice();
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
        int id = InputUtils.getInt("Nhập id sản phẩm cần sửa: ");
        try {
            Product product = productService.findById(id);
            System.out.println();
            System.out.println("Thông tin hiện tại:" + product);
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
            System.out.println("Sản phẩm tìm thấy: " + product);
            if (InputUtils.getConfirmation("Bạn có chắc chắn muốn xóa sản phẩm này?")) {
                if (productService.delete(id)) {
                    System.out.println(" Đã xóa sản phẩm");
                };
            } else {
                System.out.println("Đã hủy thao tác xóa");
            }
        } catch (RuntimeException e) {
            System.out.println("Lỗi: " + e.getMessage());
        }

    }

    private void searchProduct() {
    }

    private void filterByPrice() {
    }
}
