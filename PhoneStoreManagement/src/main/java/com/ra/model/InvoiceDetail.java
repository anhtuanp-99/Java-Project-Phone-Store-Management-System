package com.ra.model;

import java.math.BigDecimal;

public class InvoiceDetail {
    private int id;
    private int invoiceID;
    private int productID;
    private int quantity;
    private BigDecimal unitPrice;

    public InvoiceDetail(){}

    public InvoiceDetail(int invoiceID, int productID, int quantity, BigDecimal unitPrice){
        this.invoiceID = invoiceID;
        this.productID = productID;
        this.quantity = quantity;
        this.unitPrice = unitPrice;
    }

    public InvoiceDetail(int id, int invoiceID, int productID, int quantity, BigDecimal unitPrice){
        this.id = id;
        this.invoiceID = invoiceID;
        this.productID = productID;
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

    public void setProductID(int productID) {
        this.productID = productID;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public BigDecimal getUnitPrice() {
        return unitPrice;
    }

    public void setUnitPrice(BigDecimal unitPrice) {
        this.unitPrice = unitPrice;
    }

    @Override
    public String toString() {
        return "InvoiceDetails{" +
                "id=" + id +
                ", invoiceID=" + invoiceID +
                ", productID=" + productID +
                ", quantity=" + quantity +
                ", unitPrice=" + unitPrice +
                '}';
    }
}
