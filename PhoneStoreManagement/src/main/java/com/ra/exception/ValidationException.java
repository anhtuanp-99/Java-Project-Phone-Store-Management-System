package com.ra.exception;

/**
 * Ném khi dữ liệu đầu vào không hợp lệ.
 * Dùng cho: giá âm, tên trống, số lượng <= 0,...
 */
public class ValidationException extends RuntimeException{

    public ValidationException(String message) {
        super(message);
    }
}
