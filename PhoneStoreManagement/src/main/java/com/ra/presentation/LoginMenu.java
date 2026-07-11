package com.ra.presentation;

import com.ra.model.Customer;
import com.ra.repository.ICustomerRepository;
import com.ra.repository.IInvoiceRepository;
import com.ra.repository.impl.CustomerRepository;
import com.ra.repository.impl.InvoiceRepository;
import com.ra.service.ICustomerService;
import com.ra.service.impl.CustomerService;
import com.ra.utils.InputUtils;

public class LoginMenu {

    private final ICustomerService customerService;

    public LoginMenu() {
        ICustomerRepository customerRepo  = new CustomerRepository();
        IInvoiceRepository invoiceRepo = new InvoiceRepository();
        this.customerService = new CustomerService(customerRepo, invoiceRepo);
    }

    public Customer show() {
        while (true) {
            System.out.println("\n============================================");
            System.out.println("=== HỆ THỐNG QUẢN LÍ CỬA HÀNG ĐIỆN THOẠI ===");
            System.out.println("|   [1] Đăng nhập                          |");
            System.out.println("|   [2] Đăng kí tài khoản                  |");
            System.out.println("|   [3] Thoát                              |");
            System.out.println("============================================");

            int choice = InputUtils.getIntRange("Chọn chức năng: ", 1, 3);

            switch (choice) {
                case 1 -> {
                    Customer customer = handleLogin();

                    if (customer != null) return customer;
                }
                case 2 -> handleRegister();
                case 3 -> {
                    System.out.println("Tạm biệt!");
                    System.exit(0);
                }
            }

        }
    }

    private Customer handleLogin() {
        System.out.println("\n--- ĐĂNG NHẬP ---");
        String email = InputUtils.getString("Email: ");
        String password = InputUtils.getString("Mật khẩu: ");

        Customer customer = customerService.login(email, password);

        if (customer == null) {
            System.out.println("Tài khoản hoặc mật khẩu không chính xác!");
            return null;
        }

        // chỉ admin mới được vào hệ thống này
        if (!"ADMIN".equals(customer.getRole())) {
            System.out.println("Tài khoản không có quyền Admin");
            return null;
        }

        System.out.println("Đăng nhập thành công! Xin chào " + customer.getName());
        return customer;
    }

    private void handleRegister() {
        System.out.println("\n--- ĐĂNG KÍ TÀI KHOẢN ---"); // tài khoản mới mặc đinh là CUSTOMER

        Customer customer = new Customer();
        customer.setName(InputUtils.getString("Họ tên: "));
        customer.setPhone(InputUtils.getString("Số điện thoại: "));
        customer.setEmail(InputUtils.getString("Email: "));
        customer.setPassword(InputUtils.getString("Mật khẩu: "));
        customer.setAddress(InputUtils.getOptionalString("Địa chỉ (không bắt buộc): "));

        try {
            if (customerService.save(customer)) {
                System.out.println("Đăng kí thành công! Vui lòng đăng nhập");
            }
        } catch (RuntimeException e) {
            System.err.println("Lỗi: " + e.getMessage());
        }
    }

}
