package com.ra.model;

public class InvoiceDetail {
    private int id;
    private int invoiceID;
    private int productID;
    private String productName;
    private int quantity;
    private Double unitPrice;

    public InvoiceDetail(){};

    public InvoiceDetail(int invoiceID, int productID, String productName, int quantity, Double unitPrice){
        this.invoiceID = invoiceID;
        this.productID = productID;
        this.quantity = quantity;
        this.unitPrice = unitPrice;
        this.productName = productName;
    }

    public InvoiceDetail(int id, int invoiceID, int productID, String productName, int quantity, Double unitPrice){
        this.id = id;
        this.invoiceID = invoiceID;
        this.productID = productID;
        this.productName = productName;
        this.quantity = quantity;
        this.unitPrice = unitPrice;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getInvoiceID() {
        return invoiceID;
    }

    public void setInvoiceID(int invoiceID) {
        this.invoiceID = invoiceID;
    }

    public int getProductID() {
        return productID;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public void setProductID(int productID) {
        this.productID = productID;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public Double getUnitPrice() {
        return unitPrice;
    }

    public void setUnitPrice(Double unitPrice) {
        this.unitPrice = unitPrice;
    }

    @Override
    public String toString() {
        return String.format("%-30s | SL: %d | Đơn giá: %,.0f USD | Thành tiền: %,.0f USD",
                productName, quantity, unitPrice, unitPrice * quantity);
    }
}
