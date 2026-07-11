package com.ra.exception;

/**
 * Ném khi xóa entity đang được tham chiếu bởi entity khác.
 * Dùng cho: xóa Customer có hóa đơn, xóa Product có trong hóa đơn.
 */
public class ForeignKeyException extends RuntimeException{

    public ForeignKeyException(String message) {
        super(message);
    }

    public static ForeignKeyException customerHasInvoice() {
        return new ForeignKeyException("Không thể xóa khách hàng này vì đang có hóa đơn trong hệ thống.\n" +
                " Vui lòng xóa hóa đơn liên quan trước");
    }

    public static ForeignKeyException productHasInvoice() {
        return new ForeignKeyException("Không thể xóa sản phẩm này vì đã xuất hiện trong hóa đơn.\n" +
                " Hãy đặt tồn kho về 0 nếu không muốn bán nữa");
    }
}
