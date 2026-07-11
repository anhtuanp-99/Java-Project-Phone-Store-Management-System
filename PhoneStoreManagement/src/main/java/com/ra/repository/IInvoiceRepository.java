package com.ra.repository;


import com.ra.model.Invoice;
import com.ra.model.InvoiceDetail;

import java.sql.Connection;
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

    int saveWithConnection(Invoice invoice, Connection conn);
    boolean saveDetailWithConnection(InvoiceDetail detail, Connection conn);

    /*
       Kiểm tra khách hàng có hóa đơn nào không
       Dùng trước khi xóa Customer, nếu true thì từ chối xóa
       Dùng COUNT thay vì SELECT * vì chỉ cần biết có tồn tại hay không
       Không cần lấy dữ liệu -> nhanh hơn
     */
    boolean existsByCustomerId(int customerId);

    /*
        Kiểm tra sản phẩm có nằm trong hóa đơn nào không
        Dùng trước khi xóa Product
     */
    boolean existsByProductId(int productId);
}
