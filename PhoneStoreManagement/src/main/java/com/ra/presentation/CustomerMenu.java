package com.ra.presentation;

import com.ra.exception.DuplicateException;
import com.ra.exception.ForeignKeyException;
import com.ra.exception.NotFoundException;
import com.ra.exception.ValidationException;
import com.ra.model.Customer;
import com.ra.repository.ICustomerRepository;
import com.ra.repository.IInvoiceRepository;
import com.ra.repository.impl.CustomerRepository;
import com.ra.repository.impl.InvoiceRepository;
import com.ra.service.ICustomerService;
import com.ra.service.impl.CustomerService;
import com.ra.utils.InputUtils;

import java.util.List;

public class CustomerMenu {
    private final ICustomerService customerService;

    public CustomerMenu() {
        ICustomerRepository customerRepo  = new CustomerRepository();
        IInvoiceRepository invoiceRepo = new InvoiceRepository();
        this.customerService = new CustomerService(customerRepo, invoiceRepo);
    }

    public void show(){
        while (true) {
            System.out.println("\n--- Quản lí khách hàng ---");
            System.out.println("[1] Xem danh sách");
            System.out.println("[2] Thêm khách hàng");
            System.out.println("[3] Sửa thông tin");
            System.out.println("[4] Xóa khách hàng");
            System.out.println("[0] Quay lại");

            int choice = InputUtils.getIntRange("Chọn: ", 0, 4);

            switch (choice) {
                case 1 -> showAll();
                case 2 -> addCustomer();
                case 3 -> editCustomer();
                case 4 -> deleteCustomer();
                case 0 -> { return; }
            }
        }
    }

    private void showAll() {
        List<Customer> list = customerService.findAll();
        if (list.isEmpty()) {
            System.out.println("Chưa có khách hàng nào!");
            return;
        }
        list.forEach(System.out::println);
        System.out.println("Tổng: " + list.size() + " khách hàng");
    }

    private void addCustomer() {
        Customer customer = new Customer();
        System.out.println("\n--- Thêm mới khách hàng ---");
        customer.setName(InputUtils.getString("Họ tên: "));
        customer.setPhone(InputUtils.getPhone("Số điện thoại: "));
        customer.setEmail(InputUtils.getEmail("Email: "));
        customer.setPassword(InputUtils.getString("Mật khẩu: "));
        customer.setAddress(InputUtils.getOptionalString("Địa chỉ (có thể bỏ qua): "));
        try {
            if (customerService.save(customer)) {
                System.out.println("Thêm khách hàng thành công");
            }
        } catch (ValidationException | DuplicateException e) {
            System.out.println("-> Lỗi: " + e.getMessage());
        } catch (RuntimeException e) {
            System.out.println("-> Lỗi không xác định: " + e.getMessage());
        }
    }

    private void editCustomer() {
        System.out.println("--- Chỉnh sửa thông tin khách hàng ---");
        int id = InputUtils.getInt("Nhập ID khách hàng cần sửa: ");
        try {
            Customer customer = customerService.findById(id);
            System.out.println("\n-- Thông tin hiện tại --");
            System.out.println(customer);
            String name = InputUtils.getOptionalString("Họ tên mới (Enter để giữ nguyên): ");
            String phone = InputUtils.getOptionalString("Số điện thoại mới (Enter để giữ nguyên): ");
            String address = InputUtils.getOptionalString("Địa chỉ mới (Enter để giữ nguyên): ");

            if (!name.isEmpty()) customer.setName(name);
            if (!phone.isEmpty()) customer.setPhone(phone);
            if (!address.isEmpty()) customer.setAddress(address);

            if (customerService.update(customer)) {
                System.out.println("-> Cập nhật thành công!");
            }
        } catch (NotFoundException | ValidationException e) {
            System.out.println("-> Lỗi: " + e.getMessage());
        } catch (RuntimeException e) {
            System.out.println("-> Lỗi không xác định: " + e.getMessage());
        }

    }

    private void deleteCustomer() {
        System.out.println("--- Xóa khách hàng ---");
        int id = InputUtils.getInt("Nhập ID khách hàng cần xóa: ");
        try {
            Customer customer = customerService.findById(id);
            System.out.println("Khách hàng tìm thấy: " + customer);
            if (InputUtils.getConfirmation("Bạn có chắc muốn xóa khách hàng này?")) {
                if (customerService.delete(id)) {
                    System.out.println("-> Đã xóa sản phẩm");
                }
            } else {
                System.out.println("Đã hủy thao tác xóa");
            }
        } catch (NotFoundException | ForeignKeyException e) {
            // ID không tồn tại và Lỗi khóa ngoại
            System.out.println("-> Lỗi: " + e.getMessage());
        } catch (RuntimeException e) {
            // Các lỗi không xác định
            System.out.println("-> Lỗi không xác định: " + e.getMessage());
        }
    }
}
