package com.ra.service.impl;


import com.ra.exception.DuplicateException;
import com.ra.exception.ForeignKeyException;
import com.ra.exception.NotFoundException;
import com.ra.exception.ValidationException;
import com.ra.model.Customer;
import com.ra.repository.ICustomerRepository;
import com.ra.repository.IInvoiceRepository;
import com.ra.service.ICustomerService;
import com.ra.utils.ValidationUtils;
import org.mindrot.jbcrypt.BCrypt;

import java.util.List;

/*
 * BCrypt — thư viện hash password 1 chiều.
 * hashpw()      → hash plain text thành chuỗi BCrypt
 * checkpw()     → so sánh plain text với hash (không giải mã, chỉ so sánh)
 */

public class CustomerService implements ICustomerService {

    private final ICustomerRepository customerRepo;
    private final IInvoiceRepository invoiceRepo;

    public CustomerService(ICustomerRepository customerRepo,
                           IInvoiceRepository invoiceRepo){
        this.customerRepo = customerRepo;
        this.invoiceRepo = invoiceRepo;
    }

    @Override
    public List<Customer> findAll() {
        return customerRepo.findAll();
    }

    @Override
    public Customer findById(int id) {
        Customer customer = customerRepo.findById(id);
        if (customer == null) {
            throw NotFoundException.customer(id);
        }
        return customer;
    }

    @Override
    public Customer login(String email, String password) {
        Customer customer = customerRepo.findByEmail(email);

        if (customer == null) {
            return null;
        }

        // so sánh password đã nhập với password đã hash trong DB
        if (BCrypt.checkpw(password, customer.getPassword())){
            return customer;
        }
        return null;
    }

    @Override
    public Customer findByEmail(String email) {
        Customer customer = customerRepo.findByEmail(email);
        if (customer == null) {
            throw new RuntimeException("Không tìm thấy khách hàng với email " + email);
        }
        return customerRepo.findByEmail(email);
    }


    @Override
    public boolean save(Customer customer) {
        // Validation tên
        if (customer.getName() == null || customer.getName().isBlank()){
            throw new ValidationException("Tên khách hàng không được để trống!");
        }

        // Validation email sai format
        if (!ValidationUtils.isValidEmail(customer.getEmail())){
            throw new ValidationException("Email không đúng định dạng!");
        }

        // Validation phone sai format
        if (customer.getPhone() != null && !customer.getPhone().isBlank()
                && !ValidationUtils.isValidPhone(customer.getPhone())) {
            throw new ValidationException(
                    "Số điện thoại không đúng định dạng: " + customer.getPhone()
            );
        }

        // Kiểm tra email trùng
        if (customerRepo.findByEmail(customer.getEmail()) != null) {
            throw DuplicateException.email(customer.getEmail());
        }

        /*
         * Hash password trước khi lưu xuống DB.
         * BCrypt.hashpw(password, BCrypt.gensalt()):
         * - gensalt() tạo ra một "salt" ngẫu nhiên
         * - hashpw() kết hợp password + salt → tạo hash
         * Mỗi lần gọi gensalt() cho kết quả khác nhau →
         * cùng 1 password nhưng hash mỗi lần sẽ khác nhau (bảo mật hơn).
         */
        String hashedPassword = BCrypt.hashpw(customer.getPassword(), BCrypt.gensalt());
        customer.setPassword(hashedPassword);
        customer.setRole("CUSTOMER");
        return customerRepo.save(customer);
    }

    @Override
    public boolean update(Customer customer) {
        findById(customer.getId()); // kiểm tra khách hàng tồn tại trước khi update

        // Validate phone khi update nếu có nhập mới
        if (customer.getPhone() != null && !customer.getPhone().isBlank()
                && !ValidationUtils.isValidPhone(customer.getPhone())){
            throw new ValidationException(
                    "Số điện thoại không đúng định dạng: " + customer.getPhone()
            );
        }
        return customerRepo.update(customer);
    }

    @Override
    public boolean delete(int id) {
        findById(id);
        if (invoiceRepo.existsByCustomerId(id)) {
            throw ForeignKeyException.customerHasInvoice();
        }
        return customerRepo.delete(id);
    }
}
