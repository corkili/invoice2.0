package org.hld.invoice.service;

import org.hld.invoice.common.model.Result;
import org.hld.invoice.entity.Invoice;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.sql.Date;
import java.util.List;

public interface InvoiceService {

    void onLogin(int userId);

    void onLogout(int userId);

    List<String> getNames();

    Result getInvoice(int userId, long id);

    Result saveInvoice(Invoice invoice);

    Result getInvoiceByParseImage(HttpServletRequest request, MultipartFile image);

    Result batchSaveInvoices(int userId, HttpServletRequest request, MultipartFile excel);

    void deleteInvoice(Long id);

    void modifyInvoice(Invoice invoice);

    List<Invoice> getInvoiceList(int userId);

    Result searchInvoice(int userId, Date start, Date end, String self, String it);

    Result analyzeForChart(String pattern, List<Invoice> incomeInvoices, List<Invoice> outcomeInvoices);

    Result analyzeForReport(String pattern, List<Invoice> incomeInvoices, List<Invoice> outcomeInvoices, Date startDate, Date endDate);

    public Result checkInvoice(Invoice invoice, boolean checkExist);

}
