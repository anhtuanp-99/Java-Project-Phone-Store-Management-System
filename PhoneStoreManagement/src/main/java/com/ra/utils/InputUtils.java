package com.ra.utils;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Scanner;

public class InputUtils {
    private static final Scanner scanner = new Scanner(System.in);

    private InputUtils(){}

    // Nhập chuỗi từ người dùng bắt buộc không được để trống
    public static String getString(String prompt){
        while (true) {
            System.out.print(prompt);
            String input = scanner.nextLine().trim();

            if (!input.isEmpty()) {
                return input;
            }
            System.out.println("Không được để trống. Vui lòng nhập lại!");
        }
    }

    // Nhập chuỗi từ người dùng cho phép để trống
    public static String getOptionalString(String prompt) {
        System.out.print(prompt);
        return scanner.nextLine().trim();
    }

    // Nhập một số nguyên từ người dùng
    public static int getInt(String prompt){
        while (true){
            System.out.print(prompt);
            String input = scanner.nextLine().trim();
            try {
                return Integer.parseInt(input);
            } catch (NumberFormatException e){
                System.out.println("Vui lòng nhập số nguyên hợp lệ!");
            }
        }
    }

    // Nhập một số nguyên trong khoảng min-max
    public static int getIntRange(String prompt, int min, int max){
        while (true){
            int value = getInt(prompt);
            if (value >= min && value <= max) {
                return value;
            }
            System.out.printf("Vui lòng nhập số từ %d đến %d%n", min, max);
        }
    }

    // Nhập một số thực double
    public static double getDouble(String prompt){
        while (true){
            System.out.print(prompt);
            String input = scanner.nextLine();
            try {
                return Double.parseDouble(input);
            } catch (NumberFormatException e) {
                System.out.println("Vui lòng nhập số hợp lệ!");
            }
        }
    }

    // Nhận xác nhận Y/N từ người dùng
    public static boolean getConfirmation(String prompt){
        while (true) {
            System.out.print(prompt + " Y/N: ");
            String input = scanner.nextLine().trim().toUpperCase();
            if (input.equals("Y")) return true;
            if (input.equals("N")) return false;
            System.out.println("Chỉ nhập Y hoặc N!");
        }
    }

    public static LocalDate inputDate(String prompt) {
        DateTimeFormatter inputFmt = DateTimeFormatter.ofPattern("dd/MM/yyyy");

        while (true) {
            System.out.print(prompt + "(0 = hủy): ");
            String input = scanner.nextLine().trim();

            if (input.equals("0")) return null;

            try {
                return LocalDate.parse(input, inputFmt);
            } catch (DateTimeParseException e) {
                System.out.println("Sai định dạng. Vui lòng nhập dd/MM/yyyy");
            }
        }
    }

    public static String getEmail(String prompt) {
        while (true) {
            System.out.print(prompt);
            String input = scanner.nextLine().trim();

            if (input.isEmpty()) {
                System.out.println("-> Email không được để trống");
                continue;
            }

            /*
             * Dùng ValidationUtils để kiểm tra định dạng.
             * InputUtils không tự validate — chỉ gọi ValidationUtils.
             * Đúng nguyên tắc Single Responsibility:
             * InputUtils lo nhận input, ValidationUtils lo validate.
             */
            if (!ValidationUtils.isValidEmail(input)) {
                System.out.println("-> Email không đúng định dạng. " +
                        "Ví dụ đúng: abc@gmail.com");
                continue;
            }

            return input;
        }
    }

    /**
     * Nhận số điện thoại từ người dùng, validate định dạng VN.
     * Hỏi lại đến khi nhập đúng.
     * @param prompt câu hỏi hiển thị
     * @return số điện thoại hợp lệ
     */
    public static String getPhone(String prompt) {
        while (true) {
            System.out.print(prompt);
            String input = scanner.nextLine().trim();

            if (input.isEmpty()) {
                System.out.println("-> Số điện thoại không được bỏ trống");
                continue;
            }

            if (!ValidationUtils.isValidPhone(input)) {
                System.out.println("-> Số điện thoại không đúng định dạng. " +
                        "Ví dụ đúng: 091234567 (10 số, đầu 03/05/07/08/09)");
                continue;
            }

            return input;
        }
    }

}
