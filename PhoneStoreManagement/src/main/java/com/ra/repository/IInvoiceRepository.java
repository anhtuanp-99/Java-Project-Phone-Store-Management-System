package com.ra.repository;


import com.ra.model.Invoice;
import com.ra.model.InvoiceDetail;

import java.util.List;

// Interface định nghĩa các thao tác với bảng INVOICE VÀ INVOICE_DETAILS
public interface IInvoiceRepository {

    // Lấy toàn bộ danh sách hóa đơn JOIN với CUSTOMER
    List<Invoice> findAll();

    // Lưu hóa đơn mới vào bảng INVOICE
    int save(Invoice invoice);

    // Lưu từng dòng chi tiết vào bảng INVOICE_DETAILS
    boolean saveDetail(InvoiceDetail detail);

    // Lấy toàn bộ chi tiết của một hóa đơn theo invoiceId
    List<InvoiceDetail> findDetailsByInvoiceId(int invoiceId);

//    // Tìm hóa đơn theo tên khách hàng
//    Invoice findByCustomer(String name);
//
//    // Tìm hóa đơn theo ngày tháng
//    Invoice findByDate();
//
//    // Hiển thị tổng doanh thu của tất cả các ngày mà cửa hàng kinh doanh giống với mô tả
//
//    // Hiển thị tổng doanh thu của tất cả các tháng mà cửa hàng kinh doanh giống với mô tả
//
//    // Hiển thị tổng doanh thu của tất cả các năm mà cửa hàng kinh doanh giống với mô tả




}
