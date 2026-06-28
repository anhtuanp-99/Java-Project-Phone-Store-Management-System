package com.ra;

import com.ra.config.DBConnection;
import com.ra.model.Invoice;
import com.ra.model.InvoiceDetail;
import com.ra.model.Product;
import com.ra.presentation.ProductMenu;
import com.ra.service.IProductService;
import com.ra.utils.InputUtils;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class Main {

    private static final ProductMenu p = new ProductMenu();

    public static void main(String[] args) {
        DBConnection.testConnection();

//        // Kiểm tra InvoiceDetail
//        InvoiceDetail detail = new InvoiceDetail();
//        System.out.println(detail);

        p.productMenu();

    }

    public static void login(){

    }

//    public static void show(){
//        System.out.println("1. Quản lí sản phẩm");
//        System.out.println("2. Quản lí khách hàng");
//        System.out.println("3. Quản lí hóa đơn");
//        System.out.println("4. Đăng xuất");
//
//        int choice = InputUtils.getIntRange("Chọn chức năng: ", 1, 2);
//
//        switch (choice) {
//            case 1 -> productMenu();
//            case 2 -> productMenu();;
//        }
//
//    }





}