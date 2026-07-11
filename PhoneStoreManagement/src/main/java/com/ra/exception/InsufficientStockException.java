package com.ra.exception;

/**
 * Ném khi tồn kho không đủ để tạo hóa đơn.
 * Chứa thêm thông tin: tên sản phẩm, còn bao nhiêu, cần bao nhiêu.
 */
public class InsufficientStockException extends RuntimeException{

    // lưu thêm thông tin chi tiết để Presentation có thể hiển thị rõ hơn
    private final String productName;
    private final int available;
    private final int required;

    public InsufficientStockException(String productName, int available, int required) {
        // tạo message chuẩn tự động từ các tham số
        super(String.format("Sản phẩm %s không đủ tồn kho. Còn: %d, Cần: %d",
                productName, available, required));
        this.productName = productName;
        this.available = available;
        this.required = required;
    }

    // Getter để Presentation lấy thông tin cần thiết nếu cần
    public String getProductName() {
        return productName;
    }

    public int getAvailable() {
        return available;
    }

    public int getRequired() {
        return required;
    }
}
