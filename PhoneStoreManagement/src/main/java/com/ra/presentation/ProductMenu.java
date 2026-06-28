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

    public void productMenu() {
        while (true) {
            System.out.println("--- Quản lí sản phẩm ---");
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
    }

    private void updateProduct() {
    }

    private void deleteProduct() {
    }

    private void searchProduct() {
    }

    private void filterByPrice() {
    }
}
