package com.ra.service.impl;

import com.ra.model.Invoice;
import com.ra.model.InvoiceDetail;
import com.ra.model.Product;
import com.ra.repository.ICustomerRepository;
import com.ra.repository.IInvoiceRepository;
import com.ra.repository.IProductRepository;
import com.ra.service.IInvoiceService;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
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
        /*
         * items = danh sách sản phẩm muốn mua.
         * Mỗi phần tử là int[] gồm 2 giá trị: [productId, quantity]
         * Quy trình tạo hóa đơn:
         * 1. Kiểm tra khách hàng tồn tại
         * 2. Kiểm tra từng sản phẩm: tồn tại + đủ tồn kho
         * 3. Tính tổng tiền
         * 4. Lưu vào bảng INVOICE → lấy invoiceId
         * 5. Lưu từng dòng vào INVOICE_DETAILS
         * 6. Trừ stock từng sản phẩm
         */

        // kiểm tra khách hàng
        if (customerRepo.findById(customerId) == null) {
            throw new RuntimeException("Không tìm thấy khách hàng có ID " + customerId);
        }

        // Kiểm tra sản phẩm và tính tổng tiền
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

            // Tạo dòng chi tiết cho hóa đơn này
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

    // Stream tìm kiếm theo tên khách hàng
    @Override
    public List<Invoice> searchByCustomerName(String name) {
        String lowerName = name.toLowerCase().trim();
        return invoiceRepo.findAll()
                .stream().filter(inv -> inv.getCustomerName()
                        .toLowerCase()
                        .contains(lowerName)).collect(Collectors.toList());
    }

    // Stream tìm kiếm theo ngày tháng năm
    @Override
    public List<Invoice> searchByDate(LocalDate date) {
        return invoiceRepo.findAll()
                .stream()
                .filter(inv -> inv.getCreatedAt().toLocalDate().equals(date))
                .collect(Collectors.toList());
    }

    // Strem tìm kiếm theo tháng + năm
    @Override
    public List<Invoice> searchByMonthYear(int month, int year) {
        return invoiceRepo.findAll()
                .stream()
                .filter(inv ->
                        inv.getCreatedAt().getMonthValue() == month &&
                        inv.getCreatedAt().getYear() == year
                )
                .collect(Collectors.toList());
    }

    // Tìm kiếm theo khoảng ngày [from, to]
    // isAfter/isBefore không bao gồm đầu/cuối → dùng !isBefore và !isAfter
    // để bao gồm cả ngày from và ngày to
    @Override
    public List<Invoice> searchByDateRange(LocalDate from, LocalDate to) {
        return invoiceRepo.findAll()
                .stream()
                .filter(inv -> {
                    LocalDate invoiceDate = inv.getCreatedAt().toLocalDate();
                    return !invoiceDate.isBefore(from) && !invoiceDate.isAfter(to);
                })
                .collect(Collectors.toList());
    }

    // Thống kê tổng doanh thu theo từng ngày đã có hóa đơn
    @Override
    public Map<String, Double> revenueGroupByDay() {
        return invoiceRepo.findAll()
                .stream()
                .collect(Collectors.groupingBy(
                        inv -> inv.getCreatedAt()
                                .toLocalDate()
                                .format(java.time.format.DateTimeFormatter
                                        .ofPattern("dd/MM/yyyy")),
                        java.util.TreeMap::new,   // TreeMap → tự động sắp xếp theo key
                        Collectors.summingDouble(Invoice::getTotalAmount)
                ));
    }

    // thống kê theo từng tháng đã có hóa đơn
    @Override
    public Map<String, Double> revenueGroupByMonth() {
        return invoiceRepo.findAll()
                .stream()
                .collect(Collectors.groupingBy(
                   inv -> String.format("%02d/%d",
                           inv.getCreatedAt().getMonthValue(),
                           inv.getCreatedAt().getYear()),
                        java.util.TreeMap::new,
                        Collectors.summingDouble(Invoice::getTotalAmount)

                ));
    }

    @Override
    public Map<String, Double> revenueGroupByYear() {
        return invoiceRepo.findAll()
                .stream()
                .collect(Collectors.groupingBy(
                inv -> String.valueOf(inv.getCreatedAt().getYear()),
                        java.util.TreeMap::new,
                        Collectors.summingDouble(Invoice::getTotalAmount)
                ));
    }

}
