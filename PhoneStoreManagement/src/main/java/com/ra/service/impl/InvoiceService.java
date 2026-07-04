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
import com.ra.service.IProductService;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class InvoiceService implements IInvoiceService {

    private final IInvoiceRepository invoiceRepo;
    private final IProductRepository productRepo;
    private final ICustomerRepository customerRepo;


    public InvoiceService(ICustomerRepository customerRepo,
                          IProductRepository productRepo,
                          IInvoiceRepository invoiceRepo) {
        this.invoiceRepo = invoiceRepo;
        this.productRepo = productRepo;
        this.customerRepo = customerRepo;
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
        double totalAmount = 0;

        for (int[] item : items) {
            int productId = item[0];
            int quantity = item[1];

            Product product = productRepo.findId(productId);
            if (product == null) {
                throw new RuntimeException("Không tìm thấy sản phẩm có ID: " + productId);
            }

            // kiểm tra tồn kho
            if (product.getStock() < quantity) {
                throw new RuntimeException(
                    String.format("Sản phẩm %s không đủ tồn kho. Còn: %d, Cần: %d", product.getName(),
                            product.getStock(), quantity)
                );
            }

            InvoiceDetail detail = new InvoiceDetail();
            detail.setProductID(productId);
            detail.setProductName(product.getName());
            detail.setQuantity(quantity);
            detail.setUnitPrice(product.getPrice());
            details.add(detail);

            totalAmount = totalAmount + quantity * product.getPrice();
        }

        // lưu hóa đơn và lấy về id
        Invoice invoice = new Invoice();
        invoice.setCustomerId(customerId);
        invoice.setTotalAmount(totalAmount);

        int invoiceId = invoiceRepo.save(invoice);

        if (invoiceId == -1) {
            throw new RuntimeException("Lỗi khi tạo hóa đơn!");
        }

        // lưu chi tiết và trừ stock
        for (InvoiceDetail detail : details) {
            detail.setInvoiceID(invoiceId);

            invoiceRepo.saveDetail(detail);

            // stock mới = stock cũ - số lượng mua
            Product product = productRepo.findId(detail.getProductID());
            productRepo.updateStock(detail.getProductID(), product.getStock() - detail.getQuantity());
        }
        return true;
    }

    @Override
    public List<Invoice> searchByCustomerName(String name) {
        String lowerName = name.toLowerCase().trim();
        return invoiceRepo.findAll()
                .stream().filter(inv -> inv.getCustomerName()
                        .toLowerCase()
                        .contains(lowerName)).collect(Collectors.toList());
    }

    @Override
    public double revenueByDay(int day, int month, int year) {
        return invoiceRepo.findAll()
                .stream()
                .filter(inv -> {
                    var date = inv.getCreatedAt();
                    return date.getDayOfMonth() == day
                        && date.getMonthValue() == month
                        && date.getYear() == year;
                })
                .mapToDouble(Invoice::getTotalAmount)
                .sum();
    }

    @Override
    public double revenueByMonth(int month, int year) {
        return invoiceRepo.findAll()
                .stream()
                .filter(inv ->
                                inv.getCreatedAt().getMonthValue() == month &&
                                inv.getCreatedAt().getYear() == year
                )
                .mapToDouble(Invoice::getTotalAmount)
                .sum();
    }

    @Override
    public double revenueByYear(int year) {
        return invoiceRepo.findAll()
                .stream()
                .filter(inv -> inv.getCreatedAt().getYear() == year)
                .mapToDouble(Invoice::getTotalAmount)
                .sum();
    }

}
