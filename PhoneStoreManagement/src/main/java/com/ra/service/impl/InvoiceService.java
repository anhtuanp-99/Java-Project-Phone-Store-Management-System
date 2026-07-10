package com.ra.service.impl;

import com.ra.config.DBConnection;
import com.ra.model.Invoice;
import com.ra.model.InvoiceDetail;
import com.ra.model.Product;
import com.ra.repository.ICustomerRepository;
import com.ra.repository.IInvoiceRepository;
import com.ra.repository.IProductRepository;
import com.ra.service.IInvoiceService;

import java.sql.Connection;
import java.sql.SQLException;
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

        // Bước 1: Validate đầu vào trước khi mở Transaction
        if (customerRepo.findById(customerId) == null) {
            throw new RuntimeException("Không tìm thấy khách hàng có ID " + customerId);
        }
        if (items == null || items.isEmpty()) {
            throw new RuntimeException("Hóa đơn không có sản phẩm nào");
        }

        /*
            Bước 2: Chuẩn bị dữ liệu trước khi mở Transaction
            Kiểm tra sản phẩm và tính tiền bên ngoài Transaction - giảm thời gian giữ Transaction
            ( Transaction càng ngắn càng tốt )
         */
        List<InvoiceDetail> details = new ArrayList<>();
        double totalAmount = 0;

        for (int[] item : items) {
            int productId = item[0];
            int quantity = item[1];

            if (quantity <= 0) {
                throw new RuntimeException("Số lượng phải lớn hơn 0");
            }

            Product product = productRepo.findById(productId);

            if (product == null) {
                throw new RuntimeException("Không tìm thấy sản phẩm có ID: " + productId);
            }
            // kiểm tra tồn kho có đáp ứng số lượng cần mua không
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

            totalAmount += quantity * product.getPrice();
        }

        /*
            Bước 3: Mở Transaction, tất cả thao tác DB từ đây trở đi
            dùng chung một Connection và chỉ được commit khi tất cả thành công
            Try-with-resource đảm bảo conn luôn đóng khi ra khỏi khối try, dù commit hay rollback
            hay có exception bất ngờ
         */

        try (Connection conn = DBConnection.getConnection()){

            /*
                Tắt autoCommit, mặc định mỗi SQL statement tự commit ngay
                Sau khi tắt, các statement chỉ commit khi gọi conn.commit() tường minh
             */
            conn.setAutoCommit(false);

            try {
                // Bước 4a: INSERT vào bảng invoice lấy invoiceId
                Invoice invoice = new Invoice();
                invoice.setCustomerId(customerId);
                invoice.setTotalAmount(totalAmount);

                int invoiceId = invoiceRepo.saveWithConnection(invoice, conn);
                if (invoiceId == -1) {
                    throw new RuntimeException("Không thể tạo hóa đơn!");
                }

                // Bước 4b: INSERT từng dòng vào INVOICE_DETAILS
                for (InvoiceDetail detail : details) {
                    detail.setInvoiceID(invoiceId);
                    invoiceRepo.saveDetailWithConnection(detail, conn);
                }

                // Bước 4c: UPDATE stock từng sản phẩm
                for (InvoiceDetail detail : details) {
                    /*
                        Đọc stock hiện tại trong cùng Transaction (dùng WithConnection)
                        để tránh race-condition - stock có thể đã thay đổi
                        kể từ lúc kiểm tra ở bước 2
                     */

                    Product current = productRepo.findByIdWithConnection(detail.getProductID(), conn);
                    productRepo.updateStockWithConnection(
                            detail.getProductID(),
                            current.getStock() - detail.getQuantity(),
                            conn
                    );
                }

                /*
                    Bước 5: COMMIT - chỉ chạy đến đây nếu tất cả bước trên thành công
                    Lúc này tất cả thay đổi mới thực sự được lưu vào DB
                 */
                conn.commit();
                return true;
            } catch (Exception e) {
            /*
                Bước 6: ROLLBACK, bất kì exception nào (từ Repository hay logic) đều khiến
                toàn bộ Transaction bị hủy
                DB trở về trạng thái trước khi bắt đầu - sạch hoàn toàn
             */
                try {
                    conn.rollback();
                    System.out.println("    Đã rollback Transaction do lỗi: " + e.getMessage());
                } catch (SQLException rollbackEx) {
                    System.out.println("    Lỗi khi rollback: " + rollbackEx.getMessage());
                }
                // Ném lại exception gốc để Presentation hiển thị cho người dùng
                throw new RuntimeException(e.getMessage());

            } finally {
                /*
                    Bước 7: Khôi phục autoCommit về true sau khi Transaction kết thúc
                    Quan trọng vì Connection có thể được tái sử dụng sau này
                    nếu không reset thì các thao tác tiếp theo cũng không tự commit
                 */
                try {
                    conn.setAutoCommit(true);
                } catch (SQLException e) {
                    System.out.println("    Lỗi khi reset autoCommit: " + e.getMessage());
                }
            }

        } catch (SQLException e) {
            throw new RuntimeException("Lỗi khi kết nối database: " + e.getMessage());
        }

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
        if (from.isAfter(to)) {
            throw new RuntimeException("Ngày bắt đầu không được sau ngày kết thúc.");
        }
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
