package com.ra.service.impl;

import com.ra.model.Invoice;
import com.ra.model.InvoiceDetail;
import com.ra.model.Product;
import com.ra.repository.ICustomerRepository;
import com.ra.repository.IInvoiceRepository;
import com.ra.repository.IProductRepository;
import com.ra.repository.impl.CustomerRepository;
import com.ra.repository.impl.InvoiceRepository;
import com.ra.repository.impl.ProductRepository;
import com.ra.service.IInvoiceService;

import java.util.ArrayList;
import java.util.List;
import java.util.PrimitiveIterator;

public class InvoiceService implements IInvoiceService {

    private final IInvoiceRepository invoiceRepo;
    private final IProductRepository productRepo;
    private final ICustomerRepository customerRepo;


    public InvoiceService() {
        this.invoiceRepo = new InvoiceRepository();
        this.productRepo = new ProductRepository();
        this.customerRepo = new CustomerRepository();
    }


    @Override
    public List<Invoice> findAll() {
        return invoiceRepo.findAll();
    }

    @Override
    public int save(Invoice invoice) {
        int save = invoiceRepo.save(invoice);
        if (save == -1){
            throw new RuntimeException("Thêm chi tiết hóa đơn thất bại!");
        }
        return save;
    }

    @Override
    public boolean createInvoice(int customerId, List<int[]> items) {
        // kiểm tra khách hàng
        if (customerRepo.findById(customerId) == null) {
            throw new RuntimeException("Không tìm thấy khách hàng có ID " + customerId);
        }

        List<InvoiceDetail> details = new ArrayList<>();


        for (int[] item : items) {
            int productId = item[0];
            int quantity = item[1];

            Product product = productRepo.findId(productId);
            if (product == null) {
                throw new RuntimeException("Không tìm thấy sản phẩm có ID: " + productId);
            }

            if (product.getStock() < quantity) {
                throw new RuntimeException(
                    String.format("Sản phẩm %s không đủ tồn kho. Còn: %d, Cần: %d", product.getName(),
                            product.getStock(), quantity)
                );
            }


        }


        return false;
    }


}
