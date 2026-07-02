package com.ra.repository;


import com.ra.model.Invoice;
import com.ra.model.InvoiceDetail;

import java.util.List;

// Interface định nghĩa các thao tác với bảng INVOICE VÀ INVOICE_DETAILS
public interface IInvoiceRepository {

    // Lấy toàn bộ danh sách hóa đơn JOIN với CUSTOMER
    List<Invoice> findAll();

    // Tìm theo id
    Invoice findById(int id);

    // Lưu hóa đơn mới vào bảng INVOICE
    int save(Invoice invoice);

    // Lưu từng dòng chi tiết vào bảng INVOICE_DETAILS
    boolean saveDetail(InvoiceDetail detail);

    // Lấy toàn bộ chi tiết của một hóa đơn theo invoiceId
    List<InvoiceDetail> findDetailsByInvoiceId(int invoiceId);

}
