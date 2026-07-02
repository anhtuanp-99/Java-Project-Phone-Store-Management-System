package com.ra.presentation;

import com.ra.utils.InputUtils;

public class MainMenu {
    private final ProductMenu productMenu = new ProductMenu();
    private final CustomerMenu customerMenu = new CustomerMenu();
    private final InvoiceMenu invoiceMenu = new InvoiceMenu();

    public void show() {
        while (true) {
            System.out.println("\n========== PHẦN MỀM QUẢN LÍ ==========");
            System.out.println("    1. Quản lí Sản phẩm");
            System.out.println("    2. Quản lí Khách hàng");
            System.out.println("    3. Quản lí hóa đơn");
            System.out.println("    4. Đăng xuất");
            System.out.println("=======================================");

            int choice = InputUtils.getIntRange("Chọn: ", 1, 4);

            switch (choice) {
                case 1 -> productMenu.show();
                case 2 -> customerMenu.show();
                case 3 -> invoiceMenu.show();
                case 4 -> {
                    System.out.println("Đã đăng xuất.");
                    return; } // Quay về login menu
            }

        }
    }
}
