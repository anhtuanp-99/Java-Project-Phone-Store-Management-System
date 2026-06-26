package com.ra.repository.impl;

import com.ra.config.DBConnection;
import com.ra.model.Invoice;
import com.ra.model.InvoiceDetail;
import com.ra.repository.IInvoiceRepository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class InvoiceRepository implements IInvoiceRepository {
    @Override
    public List<Invoice> findAll() {
        List<Invoice> list = new ArrayList<>();
        String sql = """
                     SELECT i.id, i.customer_id, c.name AS customer_name, 
                            i.created_at, i.total_amount
                     FROM invoice i
                     JOIN customer c ON c.id = i.customer_id
                     ORDER BY i.created_at DESC
                     """;
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()){
            while (rs.next()){
                Invoice inv = new Invoice();
                inv.setId(rs.getInt("id"));
                inv.setCustomerId(rs.getInt("customer_id"));
                inv.setCustomerName(rs.getString("customer_name"));
                inv.setTotalAmount(rs.getDouble("total_amount"));
                inv.setCreatedAt(rs.getTimestamp("created_ad").toLocalDateTime());
                list.add(inv);
            }
        } catch (SQLException e){
            System.out.println("Lỗi khi lấy danh sách hóa đơn: " + e.getMessage());
        }
        return list;
    }

    @Override
    public int save(Invoice invoice) {
        String sql = "INSERT INTO invoice(customer_id, total_amount) VALUES (?, ?)";
        try (Connection conn = DBConnection.getConnection();
            PreparedStatement stmt = conn.prepareStatement(sql)){

            stmt.setInt(1, invoice.getCustomerId());
            stmt.setDouble(2, invoice.getTotalAmount());

            try (ResultSet rs = stmt.executeQuery()){
                if (rs.next()){
                    return rs.getInt(1);
                }
            }
        } catch (SQLException e){
            System.out.println("Lỗi khi tạo hóa đơn: " + e.getMessage());
        }
        return -1; // thất bại
    }

    @Override
    public boolean saveDetail(InvoiceDetail detail) {
        String sql = "INSERT INTO invoice_details(invoice_id, product_id, quantity, unit_price) VALUES (?, ?, ?, ?)";
        try (Connection conn = DBConnection.getConnection();
        PreparedStatement stmt = conn.prepareStatement(sql)){

            stmt.setInt(1, detail.getInvoiceID());
            stmt.setInt(2, detail.getProductID());
            stmt.setInt(3, detail.getQuantity());
            stmt.setDouble(4, detail.getUnitPrice());

            return stmt.executeUpdate() > 0;

        } catch (SQLException e){
            System.out.println("Lỗi khi lưu chi tiết hóa đơn: " + e.getMessage());
            return false;
        }
    }

    @Override
    public List<InvoiceDetail> findDetailsByInvoiceId(int invoiceId) {
        List<InvoiceDetail> list = new ArrayList<>();
        String sql = """
                     SELECT d.id, d.invoice_id, d.procduct_id,
                            ,p.name AS product_name, d.quantity, d.unit_price
                     FROM invoice_details d           
                     JOIN product p ON p.id = d.product_id
                     WHERE d.invoice_id = ?     
                     """;
        try (Connection conn = DBConnection.getConnection();
            PreparedStatement stmt = conn.prepareStatement(sql);
            ResultSet rs = stmt.executeQuery()){

            while (rs.next()){
                InvoiceDetail d = new InvoiceDetail();
                d.setId(rs.getInt("id"));
                d.setInvoiceID(rs.getInt("invoice_id"));
                d.setProductID(rs.getInt("product_id"));
                d.setProductName(rs.getString("product_name"));
                d.setQuantity(rs.getInt("quantity"));
                d.setUnitPrice(rs.getDouble("unit_price"));
                list.add(d);
            }

        } catch (SQLException e){
            System.out.println("Lỗi khi lấy chi tiết hóa đơn: " + e.getMessage());
        }
        return list;
    }

}
