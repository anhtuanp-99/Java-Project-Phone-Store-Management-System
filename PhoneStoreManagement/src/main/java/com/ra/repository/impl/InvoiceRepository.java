package com.ra.repository.impl;

import com.ra.config.DBConnection;
import com.ra.model.Invoice;
import com.ra.model.InvoiceDetail;
import com.ra.repository.IInvoiceRepository;

import java.awt.image.DataBuffer;
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

        return 0;
    }

    @Override
    public boolean saveDetail(InvoiceDetail detail) {
        return false;
    }

    @Override
    public Invoice findById(int id) {
        return null;
    }

    @Override
    public List<InvoiceDetail> findDetailsByInvoiceId(int invoiceId) {
        return List.of();
    }
}
