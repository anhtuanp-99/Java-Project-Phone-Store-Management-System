package com.ra;

import com.ra.config.DBConnection;
import com.ra.model.Customer;

import com.ra.presentation.LoginMenu;
import com.ra.presentation.MainMenu;

import java.sql.SQLException;


public class Main {

    public static void main(String[] args) {

        DBConnection.testConnection();
        try {
            DBConnection.getConnection();
        } catch (SQLException e) {
            System.out.println("Lỗi không thể kết nối database! Thoát chương trình");
            return;
        }

        LoginMenu loginMenu = new LoginMenu();
        MainMenu mainMenu = new MainMenu();

        while (true) {
            Customer admin = loginMenu.show();
            mainMenu.show(admin); // vào menu với quyền admin
        }

    }
}