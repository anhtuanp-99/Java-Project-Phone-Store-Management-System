package com.ra.service;

import com.ra.model.Invoice;
import com.ra.model.InvoiceDetail;

import java.util.List;

public interface IInvoiceService {
    // Lấy toàn bộ danh sách hóa đơn JOIN với CUSTOMER
    List<Invoice> findAll();

    // Lưu hóa đơn mới vào bảng INVOICE
    int save(Invoice invoice);

    // Lưu từng dòng chi tiết vào bảng INVOICE_DETAILS
    boolean saveDetail(InvoiceDetail detail);

    // Tìm hóa đơn theo ID
    Invoice findById(int id);

    // Lấy toàn bộ chi tiết của một hóa đơn theo invoiceId
    List<InvoiceDetail> findDetailsByInvoiceId(int invoiceId);
}
