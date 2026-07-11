package com.ra.exception;

/**
 * Ném khi vi phạm ràng buộc UNIQUE trong DB.
 * Dùng cho: thêm email đã tồn tại
 */
public class DuplicateException extends RuntimeException {

    public DuplicateException(String message) {
        super(message);
    }

    public static DuplicateException email(String email) {
        return new DuplicateException("Email '" + email + "' đã tồn tại trong hệ thống");
    }
}
