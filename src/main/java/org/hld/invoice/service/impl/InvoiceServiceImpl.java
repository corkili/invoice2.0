package org.hld.invoice.service.impl;

import lombok.extern.log4j.Log4j;
import org.hld.invoice.common.model.Result;
import org.hld.invoice.common.ocr.Recognition;
import org.hld.invoice.common.utils.ExcelUtil;
import org.hld.invoice.dao.InvoiceDao;
import org.hld.invoice.entity.Invoice;
import org.hld.invoice.entity.InvoiceDetail;
import org.hld.invoice.service.InvoiceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.*;
import java.sql.Date;

@Service
@Log4j
public class InvoiceServiceImpl implements InvoiceService {

    private InvoiceDao invoiceDao;

    private InvoiceContext invoiceContext;

    public InvoiceServiceImpl() {
        invoiceContext = new InvoiceContext();
    }

    @Override
    public void onLogin(int userId) {
        invoiceContext.initInvoiceList(userId);
    }

    @Override
    public void onLogout(int userId) {
        invoiceContext.removeInvoiceList(userId);
    }

    @Override
    public List<String> getNames() {
        return invoiceDao.findNames();
    }

    @Override
    public Result getInvoice(int userId, long id) {
        boolean successful = false;
        String message;
        Invoice invoice = invoiceContext.getInvoice(userId, id);
        if (invoice == null) {
            invoice = invoiceDao.get(id);
        }
        if (invoice == null) {
            message = "未查询到发票" + id + "！";
        } else {
            successful = true;
            message = "已查询到发票" + id + "！";
        }
        Result result = new Result(successful);
        result.setMessage(message);
        result.add("invoice", invoice);
        return result;
    }

    @Override
    public Result saveInvoice(Invoice invoice) {
        boolean successful = false;
        String message;
        Long id = invoiceDao.save(invoice);
        if (id == null) {
            message = "保存失败！";
        } else {
            successful = true;
            message = "保存成功！";
        }
        Result result = new Result(successful);
        result.setMessage(message);
        result.add("invoice", invoice);
        return result;
    }

    @Override
    public Result getInvoiceByParseImage(HttpServletRequest request, MultipartFile image) {
        boolean successful = false;
        String message = "";
        Invoice invoice = null;
        int detailNum = Integer.parseInt(request.getParameter("detail_num"));
        if (image != null) {
            String type = image.getContentType();
            String base64 = null;
            boolean tag = false;
            try {
                base64 = Base64.getEncoder().encodeToString(image.getBytes());
                tag = true;
            } catch (IOException e) {
                tag = false;
                successful = false;
                message = "图像识别失败！";
            }
            if (!tag || !"image/jpeg".equals(type) || base64 == null || !base64.startsWith("/9j/4")) {
                message = "文件类型不正确";
            } else {
                String path = request.getSession().getServletContext().getRealPath("WEB-INF/invoiceImage");
                String fileName = image.getOriginalFilename();
                File file = new File(path, fileName);
                if (!file.exists()) {
                    file.mkdirs();
                }
                try {
                    image.transferTo(file);
                    tag = true;
                } catch (IOException e) {
                    tag = false;
                }
                if (!tag) {
                    successful = false;
                    message = "图像识别失败！";
                } else {
                    invoice = new Invoice();
                    List<InvoiceDetail> details = new ArrayList<>();
                    Map<String, Object> result = null;
                    try {
                        result = new Recognition().recognition(path + "/" + fileName, path);
                        tag = true;
                    } catch (IOException e) {
                        tag = false;
                        successful = false;
                        message = "图像识别失败！";
                    }
                    if (!tag) {
                        successful = false;
                        message = "图像识别失败！";
                    } else {
                        if (result.get("invoiceCode") != null) {
                            invoice.setInvoiceCode(result.get("invoiceCode").toString());
                        } else {
                            invoice.setInvoiceCode("");
                        }
                        if (result.get("invoiceId") != null) {
                            invoice.setInvoiceId(result.get("invoiceId").toString());
                        } else {
                            invoice.setInvoiceId("");
                        }
                        List<String> amounts = (List<String>) result.get("amounts");
//                        List<String> quantities = (List<String>)result.get("quantities");
                        List<String> unitPrices = (List<String>) result.get("unitPrices");
                        List<String> taxs = (List<String>) result.get("taxs");
                        if (amounts.size() == unitPrices.size() && amounts.size() == taxs.size()) {
                            int quantity;
                            double amount = 0;
                            double unitPrice = 0.0;
                            double tax = 0.0;
                            double taxRate = 0.0;
                            for (int i = 0; i < amounts.size(); i++) {
                                try {
                                    amount = Double.parseDouble(amounts.get(i));
                                } catch (NumberFormatException e) {
                                    amount = 0;
                                }
                                try {
                                    unitPrice = Double.parseDouble(unitPrices.get(i));
                                } catch (NumberFormatException e) {
                                    unitPrice = 0.0;
                                }
                                try {
                                    tax = Double.parseDouble(taxs.get(i));
                                } catch (NumberFormatException e) {
                                    tax = 0.0;
                                }
                                if (unitPrice != 0) {
                                    quantity = (int) (amount / unitPrice);
                                } else {
                                    quantity = 0;
                                }
                                if (amount != 0) {
                                    taxRate = new BigDecimal(tax / amount).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
                                } else {
                                    taxRate = 0.0;
                                }
                                InvoiceDetail invoiceDetail = new InvoiceDetail();
                                invoiceDetail.setAmount(amount);
                                invoiceDetail.setQuantity(quantity);
                                invoiceDetail.setUnitPrice(unitPrice);
                                invoiceDetail.setTaxSum(tax);
                                invoiceDetail.setTaxRate(taxRate);
                                details.add(invoiceDetail);
                            }
                            double totalAmount = 0.0;
                            double totalTax = 0.0;
                            for (InvoiceDetail detail : details) {
                                totalAmount += detail.getAmount();
                                totalTax += detail.getTaxSum();
                            }
                            if (details.size() < detailNum) {
                                for (int i = 0; i < detailNum - details.size(); i++) {
                                    InvoiceDetail detail = new InvoiceDetail();
                                    detail.setDetailName("");
                                    detail.setSpecification("");
                                    detail.setUnitName("");
                                    detail.setQuantity(0);
                                    detail.setUnitPrice(0.0);
                                    detail.setAmount(0.0);
                                    detail.setTaxRate(0.0);
                                    detail.setTaxSum(0.0);
                                    details.add(detail);
                                }
                            }
                            invoice.setDetails(details);
                            invoice.setTotalAmount(totalAmount);
                            invoice.setTotalTax(totalTax);
                            invoice.setTotal(totalAmount + totalTax);
                            invoice.setInvoiceDate(new Date(System.currentTimeMillis()));
                            successful = true;
                            message = "图像识别成功！";
                        }
                    }
                }
            }
        }
        Result result = new Result(successful);
        result.setMessage(message);
        result.add("invoice", invoice);
        return result;
    }

    @Override
    public Result batchSaveInvoices(int userId, HttpServletRequest request, MultipartFile excel) {
        boolean successful = false;
        String message = "";
        if (excel != null) {
            try {
                String base64 = Base64.getEncoder().encodeToString(excel.getBytes());
                if (base64 == null || !base64.startsWith("0M8R4KGxG")
                        || !base64.startsWith("UEsDBBQAB")) {
                    message = "文件类型不正确";
                } else {
                    String path = request.getSession().getServletContext().getRealPath("WEB-INF/InvoiceExcel");
                    String fileName = excel.getOriginalFilename();
                    File file = new File(path, fileName);
                    if (!file.exists()){
                        file.mkdirs();
                    }
                    excel.transferTo(file);
                    invoiceContext.clear(userId);
                    ExcelUtil excelUtil = new ExcelUtil(path + "/" + fileName);
                    List<Invoice> invoices = excelUtil.getInvoicesFromExcel();
                    List<Invoice> errorInvoice = new ArrayList<>();
                    int detailSum = 0;
                    if (invoices != null) {
                        for (Invoice invoice : invoices) {
                            if (!checkInvoice(invoice, true).isSuccessful()) {
                                errorInvoice.add(invoice);
                            } else {
                                detailSum += invoice.getDetails().size();
                                invoiceDao.save(invoice);
                            }
                        }
                        invoices.removeAll(errorInvoice);
                        invoiceContext.addAll(userId, invoices);
                    }
                    int size = invoiceContext.getInvoiceList(userId).size();
                    if (size == 0) {
                        message = "未导入任何发票，文件内容错误或所有发票已存在！";
                    } else {
                        message = "成功共导入了" + size + "张发票，共包含" + detailSum + "条明细！";
                        if (errorInvoice.size() != 0) {
                            message += "有" + errorInvoice.size() +"张发票导入失败，可能原因是相应发票已存在或发票数据错误！";
                        }
                    }
                    successful = true;
                    message = "导入成功！";
                }
            } catch (IOException e) {
                successful = false;
                message = "导入失败！";
            }
        }
        Result result = new Result(successful);
        result.setMessage(message);
        result.add("invoiceList", invoiceContext.getInvoiceList(userId));
        return result;
    }

    @Override
    public void deleteInvoice(Long id) {
        invoiceDao.delete(id);
        invoiceContext.removeInvoice(id);
    }

    @Override
    public void modifyInvoice(Invoice invoice) {
        invoiceDao.saveOrUpdate(invoice);
        invoiceContext.update(invoice);
    }

    @Override
    public List<Invoice> getInvoiceList(int userId) {
        return invoiceContext.getInvoiceList(userId);
    }

    @Override
    public Result searchInvoice(int userId, Date start, Date end, String self, String it) {
        Result result = new Result(true);
        result.setMessage("查找成功！");
        List<Invoice> incomeInvoices;
        List<Invoice> outcomeInvoices;
        if (it == null || it.trim().equals("")) {
            incomeInvoices = invoiceDao.findInvoices(false, start, end, "buyerName", self);
            outcomeInvoices = invoiceDao.findInvoices(false, start, end, "sellerName", self);
        } else {
            incomeInvoices = invoiceDao.findInvoices(false, start, end, "buyerName",self, "sellerName", it);
            outcomeInvoices = invoiceDao.findInvoices(false, start, end, "sellerName", self, "buyerName", it);
        }
        invoiceContext.clear(userId);
        invoiceContext.addAll(userId, incomeInvoices);
        invoiceContext.addAll(userId, outcomeInvoices);
        result.add("incomeInvoices", incomeInvoices)
                .add("outcomeInvoices", outcomeInvoices);
        return result;
    }


    @Override
    public Result analyzeForChart(List<Invoice> incomeInvoices, List<Invoice> outcomeInvoices) {
        boolean successful = false;
        String message = "";

        return null;
    }

    @Override
    public Result analyzeForReport(List<Invoice> incomeInvoices, List<Invoice> outcomeInvoices) {
        return null;
    }

    @Override
    public Result checkInvoice(Invoice invoice, boolean checkExist) {
        final double e = 0.01;
        Map<String, Object> map = new HashMap<>();
        boolean successful = true;
        List<String> errorMessages = new ArrayList<>();
        String checkNum = "[0-9]*";
        int count = 1;
        if (checkExist && invoiceDao.findInvoices(false, null, null,
                "invoiceCode", invoice.getInvoiceCode(), "invoiceId", invoice.getInvoiceId()).size() != 0) {
            errorMessages.add("*" + count + ": 发票已经存在");
            Result result = new Result(false);
            result.setMessage("发票校验未通过！");
            result.add("errorMessages", errorMessages);
            return result;
        }
        // 验证发票代码是否为10位
        if (invoice.getInvoiceCode().length() != 10) {
            errorMessages.add("*" + count + ": 发票代码必须为10位!");
            count++;
            successful = false;
        }
        // 验证发票代码是否只包含数字
        if (!invoice.getInvoiceCode().matches(checkNum)) {
            errorMessages.add("*" + count + ": 发票代码只能包含数字!");
            count++;
            successful = false;
        }
        // 验证发票ID是否为8位
        if (invoice.getInvoiceId().length() != 8) {
            errorMessages.add("*" + count + ": 发票号码必须为8位!");
            count++;
            successful = false;
        }
        // 验证发票ID是否只包含数字
        if (!invoice.getInvoiceId().matches(checkNum)) {
            errorMessages.add("*" + count + ": 发票号码只能包含数字!");
            count++;
            successful = false;
        }
        double totalAmount = 0.0;
        double totalTax = 0.0;
        // 验证金额和税额
        for (int i = 0; i < invoice.getDetails().size(); i++) {
            InvoiceDetail detail = invoice.getDetails().get(i);
            // 检验数值是否大于0
            if (detail.getAmount() < 0) {
                errorMessages.add("*" + count + ": 第" + (i + 1) + "条明细的金额与（数量*单价）的值不符【精度误差为" + e + "】");
                count++;
                successful = false;
            }
            // 验证数量乘单价是否等于金额(误差为0.01)
            if (Math.abs(detail.getAmount() - detail.getQuantity() * detail.getUnitPrice()) > e) {
                errorMessages.add("*" + count + ": 第" + (i + 1) + "条明细的金额与（数量*单价）的值不符【精度误差为" + e + "】");
                count++;
                successful = false;
            }
            if (Math.abs(detail.getAmount() * detail.getTaxRate() - detail.getTaxSum()) > e) {
                errorMessages.add("*" + count + ": 第" + (i + 1) + "条明细的税额与（金额*税率）的值不符【精度误差为" + e + "】");
                count++;
                successful = false;
            }
            totalAmount += detail.getAmount();
            totalTax += detail.getTaxSum();
        }
        if (Math.abs(totalAmount - invoice.getTotalAmount()) > e) {
            errorMessages.add("*" + count + ": 发票的合计金额与所有明细的金额之和不符【精度误差为" + e + "】");
            count++;
            successful = false;
        }
        if (Math.abs(totalTax - invoice.getTotalTax()) > e) {
            errorMessages.add("*" + count + ": 发票的合计税额与所有明细的税额之和不符【精度误差为" + e + "】");
            count++;
            successful = false;
        }
        if (Math.abs(totalAmount + totalTax - invoice.getTotal()) > e) {
            errorMessages.add("*" + count + ": 发票的税价合计与（合计金额+合计税额）的值不符【精度误差为" + e + "】");
            successful = false;
        }
        Result result = new Result(successful);
        result.setMessage(successful ? "发票校验通过！" : "发票校验未通过！");
        result.add("errorMessages", errorMessages);
        return result;
    }


    @Autowired
    public void setInvoiceDao(InvoiceDao invoiceDao) {
        this.invoiceDao = invoiceDao;
    }

    private class InvoiceContext {
        private Map<Integer, Map<Long, Invoice>> userInvoices;

        private InvoiceContext() {
            userInvoices = new HashMap<>();
        }

        public Invoice getInvoice(int userId, long id) {
            return userInvoices.get(userId).get(id);
        }

        public void initInvoiceList(int userId) {
            userInvoices.put(userId, new HashMap<>());
        }

        public List<Invoice> getInvoiceList(int userId) {
            Map<Long, Invoice> invoiceMap = userInvoices.get(userId);
            if (invoiceMap == null) {
                invoiceMap = new HashMap<>();
                userInvoices.put(userId, invoiceMap);
            }
            return new ArrayList<>(invoiceMap.values());
        }

        public void clear(int userId) {
            if (userInvoices.containsKey(userId)) {
                userInvoices.get(userId).clear();
            }
        }

        public void addAll(int userId, List<Invoice> invoices) {
            if (!userInvoices.containsKey(userId)) {
                userInvoices.put(userId, new HashMap<>());
            }
            Map<Long, Invoice> invoiceMap = userInvoices.get(userId);
            for (Invoice invoice : invoices) {
                invoiceMap.put(invoice.getId(), invoice);
            }
        }

        public void removeInvoiceList(int userId) {
            userInvoices.remove(userId);
        }

        public void removeInvoice(Long... ids) {
            for (Long id : ids) {
                for (Map<Long, Invoice> invoiceMap : userInvoices.values()) {
                    invoiceMap.remove(id);
                }
            }
        }

        public void removeInvoice(List<Long> ids) {
            for (Long id : ids) {
                for (Map<Long, Invoice> invoiceMap : userInvoices.values()) {
                    invoiceMap.remove(id);
                }
            }
        }

        public void update(Invoice... invoices) {
            for (Invoice invoice : invoices) {
                for (Map<Long, Invoice> invoiceMap : userInvoices.values()) {
                    if (invoiceMap.containsKey(invoice.getId())) {
                        invoiceMap.put(invoice.getId(), invoice);
                    }
                }
            }
        }

        public void update(List<Invoice> invoices) {
            for (Invoice invoice : invoices) {
                for (Map<Long, Invoice> invoiceMap : userInvoices.values()) {
                    if (invoiceMap.containsKey(invoice.getId())) {
                        invoiceMap.put(invoice.getId(), invoice);
                    }
                }
            }
        }
    }
}
