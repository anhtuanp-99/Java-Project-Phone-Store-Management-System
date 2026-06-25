package com.ra.config;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;
import java.io.InputStream;

/**
 * Lớp DBConnection quản lý kết nối đến PostgreSQL.
 * Nó đọc cấu hình từ file db.properties và cung cấp Connection.
 * Đây là cách tách biệt cấu hình, giúp ứng dụng linh hoạt hơn.
 */
public class DBConnection {

    private static String url;
    private static String username;
    private static String password;

    static {
        try (InputStream input = DBConnection.class.getClassLoader()
                .getResourceAsStream("db.properties")) {
            Properties props = new Properties();
            props.load(input);
            url = props.getProperty("db.url");
            username = props.getProperty("db.username");
            password = props.getProperty("db.password");
        } catch (Exception e) {
            throw new RuntimeException("Không thể load file cấu hình cơ sở dữ liệu!", e);
        }
    }

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(url, username, password);
    }


    public static boolean testConnection() {
        // try-with-resources tự động đóng Connection sau khi dùng
        try (Connection conn = getConnection()) {
            System.out.println("Kết nối PostgreSQL thành công!");
            return true;
        } catch (SQLException e) {
            System.err.println("Kết nối thất bại: " + e.getMessage());
            return false;
        }
    }
}