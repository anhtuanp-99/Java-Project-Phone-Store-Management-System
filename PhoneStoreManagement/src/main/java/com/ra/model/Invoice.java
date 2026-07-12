package com.ra.model;

import java.time.LocalDateTime;
import java.util.List;

public class Invoice {
    private int id;
    private int customerId;
    private String customerName;
    private LocalDateTime createdAt;
    private double totalAmount;

    // Danh sách chi tiết các sản phẩm trong hóa đơn này
    // Không lưu trong bảng INVOICE, nhưng cần để hiển thị đầy đủ thông tin
    private List<InvoiceDetail> details;

    public Invoice(){}

    public Invoice(int customerId, LocalDateTime createdAt, double totalAmount){
        this.customerId = customerId;
        this.createdAt = createdAt;
        this.totalAmount = totalAmount;
    }

    public Invoice(int id, int customerId, String customerName, LocalDateTime createdAt, double totalAmount){
        this.id = id;
        this.customerId = customerId;
        this.customerName = customerName;
        this.createdAt = createdAt;
        this.totalAmount = totalAmount;
    }

    public void setId(int id) {
        this.id = id;
    }
    public int getId() {
        return id;
    }

    public String getCustomerName() {
        return customerName;
    }
    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public void setTotalAmount(double totalAmount) {
        this.totalAmount = totalAmount;
    }
    public double getTotalAmount() {
        return totalAmount;
    }

    public int getCustomerId() {
        return customerId;
    }
    public void setCustomerId(int customerId) {
        this.customerId = customerId;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public List<InvoiceDetail> getDetails() {
        return details;
    }
    public void setDetails(List<InvoiceDetail> details) {
        this.details = details;
    }



    @Override
    public String toString() {
        return String.format("HD#%d | KH: %-20s | Ngày: %s | Tổng tiền: %,.0f USD",
                id, customerName, createdAt.toLocalDate(), totalAmount);
    }
}
