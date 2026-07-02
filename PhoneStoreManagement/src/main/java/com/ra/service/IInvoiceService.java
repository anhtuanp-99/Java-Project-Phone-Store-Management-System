package com.ra.service;

import com.ra.model.Invoice;
import com.ra.model.InvoiceDetail;

import java.util.List;

public interface IInvoiceService {
    // Lấy toàn bộ danh sách hóa đơn JOIN với CUSTOMER
    List<Invoice> findAll();

    // Lưu hóa đơn mới vào bảng INVOICE
    int save(Invoice invoice);

    // tạo 1 hóa đơn mới (kiểm tra tồn kho, tính tổng tiền và lưu vào 2 bảng)
    boolean createInvoice(int cutomerId, List<int[]> items);

    // Thống kê doanh thu theo ngày cụ thể
    double revenueByDay(int day, int month, int year);

    // Thống kê doanh thu theo tháng
    double revenueByMonth(int month, int year);

    // Thống kê doanh thu theo năm
    double revenueByYear(int year);
}
