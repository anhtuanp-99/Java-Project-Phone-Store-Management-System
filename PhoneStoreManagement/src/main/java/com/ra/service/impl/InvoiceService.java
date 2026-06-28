package com.ra.service.impl;

import com.ra.model.Invoice;
import com.ra.model.InvoiceDetail;
import com.ra.repository.IInvoiceRepository;
import com.ra.repository.impl.InvoiceRepository;
import com.ra.service.IInvoiceService;

import java.util.ArrayList;
import java.util.List;

public class InvoiceService implements IInvoiceService {

    private final IInvoiceRepository invoiceRepo;

    public InvoiceService() {
        this.invoiceRepo = new InvoiceRepository();
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
    public boolean createInvoice() {
        return false;
    }


}
