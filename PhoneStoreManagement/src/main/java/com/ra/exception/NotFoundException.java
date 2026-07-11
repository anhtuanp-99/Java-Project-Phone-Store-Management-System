package com.ra.exception;

/**
 * Ném khi không tìm thấy entity theo ID
 * Dùng cho: findById trả về null
 */
public class NotFoundException extends RuntimeException {
    /*
     * Extends RuntimeException → không cần khai báo throws ở method signature.
     * Unchecked exception phù hợp vì đây là lỗi nghiệp vụ,
     * không phải lỗi hệ thống cần xử lý bắt buộc.
     */

    public NotFoundException(String message) {
        super(message); // truyền message lên RuntimeException
    }

    // Factory method - tạo exception với message chuẩn, không cần nhớ format
    public static NotFoundException product(int id) {
        return new NotFoundException("Không tìm thấy sản phẩm với ID: " + id);
    }

    public static NotFoundException customer(int id) {
        return new NotFoundException("Không tìm thấy khách hàng với ID: " + id);
    }

    public static NotFoundException invoice(int id) {
        return new NotFoundException("Không tìm thấy hóa đơn với ID: " + id);
    }


}
