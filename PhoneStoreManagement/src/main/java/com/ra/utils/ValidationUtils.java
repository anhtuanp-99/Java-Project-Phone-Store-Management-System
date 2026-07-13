package com.ra.utils;

import java.util.regex.Pattern;

/**
 * Utility class chứa các phương thức validate định dạng dữ liệu.
 * Tách ra khỏi InputUtils vì InputUtils lo việc nhận input từ Scanner,
 * còn ValidationUtils lo việc kiểm tra tính hợp lệ của dữ liệu.
 * Tách như vậy thì mỗi class chỉ có 1 nhiệm vụ — đúng nguyên tắc SRP.
 */
public class ValidationUtils {

    private ValidationUtils() {}

    /*
     * Regex email chuẩn:
     * [a-zA-Z0-9+_.-]+ → phần local (trước @): chữ, số, +, _, ., -
     * @                 → ký tự @
     * [a-zA-Z0-9.-]+   → tên domain: chữ, số, ., -
     * \.               → dấu chấm trước extension
     * [a-zA-Z]{2,}     → extension tối thiểu 2 ký tự (com, vn, org,...)
     *
     * Ví dụ hợp lệ  : abc@gmail.com, user.name+tag@example.co.uk
     * Ví dụ không hợp lệ: abc, abc@, abc@gmail, @gmail.com
     */
    private static final Pattern EMAIL_PATTERN = Pattern.compile(
            "^[a-zA-Z0-9+_.-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$"
    );

    /*
     * Regex số điện thoại Việt Nam:
     * ^0           → bắt đầu bằng số 0
     * [3|5|7|8|9]  → đầu số hợp lệ của nhà mạng VN:
     *                03x (Viettel), 05x (Vietnamobile/Gmobile),
     *                07x (Mobifone), 08x (Vinaphone/Viettel),
     *                09x (tất cả nhà mạng)
     * [0-9]{8}     → 8 chữ số còn lại
     * $            → kết thúc chuỗi
     * Tổng: 10 chữ số
     *
     * Ví dụ hợp lệ     : 0901234567, 0387654321
     * Ví dụ không hợp lệ: 01234567890 (11 số), 1234567890 (không bắt đầu 0)
     */

    private static final Pattern PHONE_PATTERN = Pattern.compile(
             "^0[3|5|7|8|9][0-9]{8}$"
     );

    /**
     * Kiểm tra email có đúng định dạng không.
     * @param email chuỗi cần kiểm tra
     * @return true nếu hợp lệ
     */
    public static boolean isValidEmail(String email) {
        if (email == null || email.isBlank()) return false;
        /*
         * Pattern.matcher(input).matches():
         * - matcher() tạo đối tượng Matcher để so khớp
         * - matches() kiểm tra TOÀN BỘ chuỗi có khớp pattern không
         *   (khác find() chỉ tìm một phần)
         */
        return EMAIL_PATTERN.matcher(email.trim()).matches();
    }

    /**
     * Kiểm tra số điện thoại VN có đúng định dạng không.
     * @param phone chuỗi cần kiểm tra
     * @return true nếu hợp lệ
     */
    public static boolean isValidPhone(String phone) {
        if (phone == null || phone.isBlank()) return false;
        return PHONE_PATTERN.matcher(phone.trim()).matches();
    }

    /**
     * Kiểm tra giá có hợp lệ không (> 0).
     * @param price giá cần kiểm tra
     * @return true nếu hợp lệ
     */
    public static boolean isValidPrice(double price) {
        return price > 0;
    }

    /**
     * Kiểm tra số lượng có hợp lệ không (> 0).
     * @param quantity số lượng cần kiểm tra
     * @return true nếu hợp lệ
     */
    public static boolean isValidQuantity(int quantity) {
        return quantity > 0;
    }
}
