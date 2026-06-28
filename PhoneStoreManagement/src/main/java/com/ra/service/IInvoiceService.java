package com.ra.service;

import com.ra.model.Invoice;
import com.ra.model.InvoiceDetail;

import java.util.List;

public interface IInvoiceService {
    // Lấy toàn bộ danh sách hóa đơn JOIN với CUSTOMER
    List<Invoice> findAll();

    // Lưu hóa đơn mới vào bảng INVOICE
    int save(Invoice invoice);

    // tạo 1 hóa đơn mới
    boolean createInvoice();

    // Thống kê doanh thu theo ngày cụ thể

    // Thống kê doanh thu theo tháng

    // Thống kê doanh thu theo năm

}
