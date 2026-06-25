package com.ra;

import com.ra.config.DBConnection;
import com.ra.model.Invoice;
import com.ra.model.InvoiceDetail;

import java.math.BigDecimal;

public class Main {
    public static void main(String[] args) {
        DBConnection.testConnection();

        // Kiểm tra InvoiceDetail
        InvoiceDetail detail = new InvoiceDetail(1, 5, 2, new BigDecimal("250.00"));
        System.out.println(detail);
    }
}