package com.ra.presentation;

import com.ra.model.Invoice;
import com.ra.repository.ICustomerRepository;
import com.ra.repository.IInvoiceRepository;
import com.ra.repository.IProductRepository;
import com.ra.repository.impl.CustomerRepository;
import com.ra.repository.impl.InvoiceRepository;
import com.ra.repository.impl.ProductRepository;
import com.ra.service.IInvoiceService;
import com.ra.service.impl.InvoiceService;
import com.ra.utils.InputUtils;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class InvoiceMenu {

    private final IInvoiceService invoiceService;

    public InvoiceMenu() {
        IInvoiceRepository invoiceRepo = new InvoiceRepository();
        ICustomerRepository customerRepo = new CustomerRepository();
        IProductRepository productRepo = new ProductRepository();
        this.invoiceService = new InvoiceService(customerRepo, productRepo, invoiceRepo);
    }

    public void show() {
        while (true) {
            System.out.println("\n--- Quản lí hóa đơn ---");
            System.out.println("[1] Xem danh sách hóa đơn");
            System.out.println("[2] Tạo hóa đơn bán hàng");
            System.out.println("[3] Tìm kiếm hóa đơn");
            System.out.println("[4] Thống kê doanh thu");
            System.out.println("[0] Quay lại");

            int choice =  InputUtils.getIntRange("Chọn: ", 0, 4);

            switch (choice) {
                case 1 -> showAll();
                case 2 -> createInvoice();
                case 3 -> searchMenu();
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
        System.out.println("\n-- Tạo hóa đơn bán hàng --");
        int customerId = InputUtils.getInt("Nhập ID khách hàng: ");

        List<int[]> items = new ArrayList<>(); // danh sách sản phẩm muốn mua

        while (true) {
            System.out.println("\nThêm sản phẩm vào hóa đơn:");
            int productId = InputUtils.getInt("ID sản phẩm ( 0 = kết thúc): ");
            if (productId == 0) break;

            int quantity = InputUtils.getInt("Số lượng: ");
            if (quantity <= 0) {
                System.out.println("Số lượng sản phẩm phải lớn hơn 0!");
                continue;
            }

            items.add(new int[] {productId, quantity});
            System.out.println("-> Đã thêm sản phẩm vào hóa đơn");
        }

        if (items.isEmpty()) {
            System.out.println("-> Hóa đơn trống, đã hủy tạo hóa đơn!");
            return;
        }

        try {
            if (invoiceService.createInvoice(customerId, items)){
                System.out.println("-> Tạo hóa đơn thành công");
            }
        } catch (RuntimeException e) {
            System.out.println("Lỗi: " + e.getMessage());
        }

    }

    private void searchMenu() {
        while (true) {
            System.out.println("\n-- Tìm kiếm hóa đơn --");
            System.out.println("[1] Tìm kiếm theo tên");
            System.out.println("[2] Tìm kiếm ngày tháng năm");
            System.out.println("[0] Quay lại");

            int choice = InputUtils.getIntRange("Chọn: ", 0, 2);

            switch (choice) {
                case 1 -> searchByName();
                case 2 -> searchByDate();
                case 0 -> { return; }
            }
        }


    }

    private void searchByName() {
        System.out.println("\n-- Tìm kiếm hóa đơn theo tên khách hàng --");
        String name = InputUtils.getString("Nhập tên khách hàng cần tìm: ");
        List<Invoice> results = invoiceService.searchByCustomerName(name);

        if (results.isEmpty()) {
            System.out.println("-> Không tìm thấy hóa đơn nào có tên: " + name);
            return;
        }

        System.out.println("\n-- Kết quả tìm kiếm --");
        System.out.println("    Tìm thấy: " + results.size() + " hóa đơn");
        results.forEach(System.out::println);
    }

    private void searchByDate() {
        System.out.println("\n-- Tìm kiếm hóa đơn theo ngày tháng năm --");
        System.out.println("Định dạng ngày: dd/MM/yyyy");
        LocalDate date = InputUtils.inputDate("Nhập ngày: ");

        if (date == null) return;

        List<Invoice> results = invoiceService.searchByDate(date);

        if (results.isEmpty()) {
            System.out.println("Không tìm thấy hóa đơn nào trong ngày " + date);
        } else {
            System.out.printf("Hóa đơn ngày %s: %n", date);
            results.forEach(System.out::println);
            System.out.printf("Tổng: %d hóa đơn %n", results.size());
        }

    }

    private void showRevenue() {
        while (true) {
            System.out.println("\n-- Thống kê doanh thu --");
            System.out.println("1. Theo từng ngày đã kinh doanh");
            System.out.println("2. Theo từng tháng đã kinh doanh");
            System.out.println("3. Theo từng năm đã kinh doanh");
            System.out.println("0. Quay lại");

            int choice = InputUtils.getIntRange("Chọn: ", 0, 3);

            switch (choice) {
                case 1 -> printRevenue(invoiceService.revenueGroupByDay(),
                        "NGÀY", "Thống kê doanh thu theo ngày");
                case 2 -> printRevenue(invoiceService.revenueGroupByMonth(),
                        "THÁNG", "Thống kê doanh thu theo tháng");
                case 3 -> printRevenue(invoiceService.revenueGroupByYear(),
                        "NĂM", "Thống kê doanh thu theo năm");
                case 0 -> { return; }
            }
        }
    }

    private void printRevenue(
            java.util.Map<String, Double> data,
            String colHeader,
            String title) {
        if (data.isEmpty()) {
            System.out.println("Chưa có dữ liệu doanh thu");
            return;
        }

        System.out.printf("%-13s | %15s%n", colHeader, " DOANH THU (USD)");

        double total = 0;
        for (var entry : data.entrySet()) {
            System.out.printf("%-13s | %,15.2f %n",
                    entry.getKey(), entry.getValue());
            total += entry.getValue();
        }

        System.out.printf("%-13s | %,15.2f %n", "TỔNG CỘNG", total);
    }

}
