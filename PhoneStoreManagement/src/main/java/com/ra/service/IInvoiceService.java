package com.ra.service;

import com.ra.model.Invoice;

import java.time.LocalDate;
import java.time.YearMonth;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

public interface IInvoiceService {
    // Lấy toàn bộ danh sách hóa đơn JOIN với CUSTOMER
    List<Invoice> findAll();

    // Lưu hóa đơn mới vào bảng INVOICE
    int save(Invoice invoice);

    // tạo 1 hóa đơn mới (kiểm tra tồn kho, tính tổng tiền và lưu vào 2 bảng)
    boolean createInvoice(int cutomerId, List<int[]> items);

    // Tìm kiếm hóa đơn theo tên khách hàng
    List<Invoice> searchByCustomerName(String name);

    // Tìm kiếm theo ngày tháng năm
    List<Invoice> searchByDate(LocalDate date);

    List<Invoice> searchByMonthYear(int month, int year);

    List<Invoice> searchByDateRange(LocalDate form, LocalDate to);


    // Key = ngày/tháng/năm, Value = tổng doanh thu
    Map<String, Double> revenueGroupByDay();
    Map<String, Double> revenueGroupByMonth();
    Map<String, Double> revenueGroupByYear();

    // Xem danh sách hóa đơn chi tiết
    Invoice findByIdWithDetails(int id);
}
