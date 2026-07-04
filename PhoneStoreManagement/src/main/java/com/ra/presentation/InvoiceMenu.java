package com.ra.presentation;

import com.ra.model.Invoice;
import com.ra.service.IInvoiceService;
import com.ra.service.impl.InvoiceService;
import com.ra.utils.InputUtils;

import java.util.ArrayList;
import java.util.List;

public class InvoiceMenu {

    private final IInvoiceService invoiceService;

    public InvoiceMenu() {
        this.invoiceService = new InvoiceService();
    }

    public void show() {
        while (true) {
            System.out.println("\n--- Quản lí hóa đơn ---");
            System.out.println("1. Xem danh sách hóa đơn");
            System.out.println("2. Tạo hóa đơn bán hàng");
            System.out.println("3. Tìm kiếm hóa đơn theo tên khách hàng");
            System.out.println("4. Thống kê doanh thu");
            System.out.println("0. Quay lại");

            int choice =  InputUtils.getIntRange("Chọn: ", 0, 4);

            switch (choice) {
                case 1 -> showAll();
                case 2 -> createInvoice();
                case 3 -> searchInvoice();
                case 4 -> showRevenue();
                case 0 -> { return; }
            }
        }
    }

    private void showAll() {
        List<Invoice> list = invoiceService.findAll();
        if (list.isEmpty()) {
            System.out.println("Chưa có hóa đơn nào");
            return;
        }
        list.forEach(System.out::println);
        System.out.println("    Tổng: " + list.size() + " hóa đơn");
    }

    private void createInvoice() {
        System.out.println("-- Tạo hóa đơn bán hàng --");
        int customerId = InputUtils.getInt("Nhập ID khách hàng: ");

        List<int[]> items = new ArrayList<>(); // danh sách sản phẩm muốn mua

        while (true) {
            System.out.println("Thêm sản phẩm vào hóa đơn:");
            int productId = InputUtils.getInt("Nhập ID sản phẩm cần mua ( 0 = kết thúc): ");
            if (productId == 0) break;

            int quantity = InputUtils.getInt("Số lượng: ");
            if (quantity <= 0) {
                System.out.println("Số lượng sản phẩm phải lớn hơn 0!");
                continue;
            }

            items.add(new int[] {productId, quantity});
            System.out.println("Đã thêm sản phẩm vào hóa đơn");
        }

        if (items.isEmpty()) {
            System.out.println("Hóa đơn trống, đã hủy tạo hóa đơn!");
            return;
        }

        try {
            if (invoiceService.createInvoice(customerId, items)){
                System.out.println("Tạo hóa đơn thành công");
            }
        } catch (RuntimeException e) {
            System.out.println("Lỗi: " + e.getMessage());
        }

    }

    private void searchInvoice() {

    }

    private void showRevenue() {

    }



}
