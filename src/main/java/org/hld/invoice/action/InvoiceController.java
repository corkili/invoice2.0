package org.hld.invoice.action;

import lombok.extern.java.Log;
import org.hld.invoice.common.model.Result;
import org.hld.invoice.common.session.SessionContext;
import org.hld.invoice.entity.Invoice;
import org.hld.invoice.entity.InvoiceDetail;
import org.hld.invoice.entity.User;
import org.hld.invoice.service.InvoiceService;
import org.hld.invoice.service.RecordService;
import org.hld.invoice.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.sql.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

@Controller
@Log
public class InvoiceController {

    private UserService userService;

    private InvoiceService invoiceService;

    private RecordService recordService;

    private SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

    @Autowired
    public void setUserService(UserService userService) {
        this.userService = userService;
    }

    @Autowired
    public void setRecordService(RecordService recordService) {
        this.recordService = recordService;
    }

    @Autowired
    public void setInvoiceService(InvoiceService invoiceService) {
        this.invoiceService = invoiceService;
    }

    @RequestMapping(value = "/addInvoiceByHand", method = RequestMethod.GET)
    public ModelAndView addInvoiceByHandPage(@SessionAttribute(SessionContext.ATTR_USER_ID) int userId,
                                             @SessionAttribute(SessionContext.ATTR_USER_NAME) String displayName) {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.addObject("display_name", displayName);
        Result result = userService.getUser(userId);
        if (!result.isSuccessful()) {
            modelAndView.setViewName("tip");
            modelAndView.addObject("url", "login")
                    .addObject("message", "发生未知错误，请重新登录！");
            return modelAndView;
        }
        User user = (User)result.get("user");
        modelAndView.setViewName("invoice_input_hand");
        modelAndView.addObject("has_authority", user.getAuthority().getAddInvoice())
                .addObject("detail_num", 0);
        return modelAndView;
    }

    @RequestMapping(value = "/addInvoiceByHand", method = RequestMethod.POST)
    public ModelAndView addInvoiceByHand(@SessionAttribute(SessionContext.ATTR_USER_ID) int userId,
                                       @SessionAttribute(SessionContext.ATTR_USER_NAME) String displayName,
                                       @RequestParam(name = "action", required = false, defaultValue = "") String action,
                                       @RequestParam(name = "preAction", required = false, defaultValue = "") String preAction,
                                       @ModelAttribute Invoice invoice, HttpServletRequest request) {
        ModelAndView modelAndView = new ModelAndView();
        Result result = userService.getUser(userId);
        if (!result.isSuccessful()) {
            modelAndView.setViewName("tip");
            modelAndView.addObject("url", "login")
                    .addObject("message", "发生未知错误，请重新登录！");
            return modelAndView;
        }
        User user = (User)result.get("user");
        if (preAction.equals("hand") && action.equals("setDetailNum")) {
            int detailNum = Integer.parseInt(request.getParameter("detail_num"));
            modelAndView.setViewName("invoice_input_hand");
            modelAndView.addObject("has_authority", user.getAuthority().getAddInvoice())
                    .addObject("invoice", new Invoice())
                    .addObject("detail_num", detailNum)
                    .addObject("has_errors", false)
                    .addObject("error_messages", "")
                    .addObject("display_name", displayName);
        } else if (preAction.equals("hand") && action.equals("saveInvoice")) {
            Result checkResult = invoiceService.checkInvoice(invoice, true);
            if (checkResult.isSuccessful()) {
                for (InvoiceDetail detail : invoice.getDetails()) {
                    detail.setInvoiceCode(invoice.getInvoiceCode());
                    detail.setInvoiceId(invoice.getInvoiceId());
                }
                Result saveResult = invoiceService.saveInvoice(invoice);
                modelAndView.setViewName("invoice_save_result");
                if (saveResult.isSuccessful()) {
                    invoice = (Invoice)saveResult.get("invoice");
                    modelAndView.addObject("has_result", true);
                    modelAndView.addObject("invoice", invoice);
                    modelAndView.addObject("nextAction", preAction);
                    modelAndView.addObject("display_name", displayName);
                } else {
                    modelAndView.addObject("has_result", false)
                            .addObject("display_name", displayName);
                }
            } else {
                modelAndView.setViewName("invoice_input_hand");
                modelAndView.addObject("has_authority", user.getAuthority().getAddInvoice())
                        .addObject("invoice", invoice)
                        .addObject("detail_num", invoice.getDetails().size())
                        .addObject("has_errors", true)
                        .addObject("error_messages", checkResult.get("errorMessages"))
                        .addObject("display_name", displayName);
            }
        } else {
            return new ModelAndView("redirect:/main");
        }
        return modelAndView;
    }

    @RequestMapping(value = "/addInvoiceByExcel", method = RequestMethod.GET)
    public ModelAndView addInvoiceByExcelPage(@SessionAttribute(SessionContext.ATTR_USER_ID) int userId,
                                             @SessionAttribute(SessionContext.ATTR_USER_NAME) String displayName) {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.addObject("display_name", displayName);
        Result result = userService.getUser(userId);
        if (!result.isSuccessful()) {
            modelAndView.setViewName("tip");
            modelAndView.addObject("url", "login")
                    .addObject("message", "发生未知错误，请重新登录！");
            return modelAndView;
        }
        User user = (User)result.get("user");
        modelAndView.setViewName("invoice_input_excel");
        modelAndView.addObject("has_authority", user.getAuthority().getAddInvoice())
                .addObject("has_file", false)
                .addObject("result_message", "")
                .addObject("invoice_list", new ArrayList<Invoice>());
        return modelAndView;
    }

    @SuppressWarnings("unchecked")
    @RequestMapping(value = "/addInvoiceByExcel", method = RequestMethod.POST)
    public ModelAndView addInvoiceByExcel(@SessionAttribute(SessionContext.ATTR_USER_ID) int userId,
                                          @SessionAttribute(SessionContext.ATTR_USER_NAME) String displayName,
                                          @RequestParam(value = "invoice_excel", required = false) MultipartFile file,
                                          HttpServletRequest request) {
        ModelAndView modelAndView = new ModelAndView("invoice_input_excel");
        modelAndView.addObject("display_name", displayName);
        Result result = userService.getUser(userId);
        if (!result.isSuccessful()) {
            modelAndView.setViewName("tip");
            modelAndView.addObject("url", "login")
                    .addObject("message", "发生未知错误，请重新登录！");
            return modelAndView;
        }
        User user = (User)result.get("user");
        Result saveResult = invoiceService.batchSaveInvoices(userId, request, file);
        modelAndView.addObject("has_authority", user.getAuthority().getAddInvoice())
                .addObject("has_file", true)
                .addObject("result_message", saveResult.getMessage())
                .addObject("invoice_list", saveResult.get("invoiceList"))
                .addObject("error_invoice_list", saveResult.get("errorInvoiceList"));
        return modelAndView;
    }

    @RequestMapping(value = "invoiceDataTemplate.zip", method = {RequestMethod.GET, RequestMethod.POST})
    public void download(HttpServletRequest request, HttpServletResponse response) {
        response.setContentType("charset=UTF-8");
        String path = request.getSession().getServletContext().getRealPath("WEB-INF") + "\\files\\";
        File file = new File(path + "发票数据导入模板.zip");
        try {
            response.setHeader("Content-Disposition", "attachment; filename=" +
                    new String("发票数据导入模板.zip".getBytes("utf-8"), "ISO_8859_1"));
        } catch (UnsupportedEncodingException e) {
            response.setHeader("Content-Disposition", "attachment; filename=template.zip");

        }
        BufferedInputStream bis = null;
        BufferedOutputStream bos = null;
        OutputStream fos = null;
        InputStream fis = null;
        try {
            fis = new FileInputStream(file.getAbsolutePath());
            bis = new BufferedInputStream(fis);
            fos = response.getOutputStream();
            bos = new BufferedOutputStream(fos);
            int bytesRead;
            byte[] buffer = new byte[5 * 1024];
            while ((bytesRead = bis.read(buffer)) != -1) {
                bos.write(buffer, 0, bytesRead);
            }
            bos.flush();
        } catch(Exception ignored){
        } finally {
            try {
                if (bis != null) {
                    bis.close();
                }
                if (bos != null) {
                    bos.close();
                }
                if (fos != null) {
                    fos.close();
                }
                if (fis != null) {
                    fis.close();
                }
            } catch (IOException ignored) {
            }
        }
    }

    @RequestMapping(value = "/queryForList", method = RequestMethod.GET)
    public ModelAndView queryForListPage(@SessionAttribute(SessionContext.ATTR_USER_ID) int userId,
                                         @SessionAttribute(SessionContext.ATTR_USER_NAME) String displayName) {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.addObject("display_name", displayName);
        Result result = userService.getUser(userId);
        if (!result.isSuccessful()) {
            modelAndView.setViewName("tip");
            modelAndView.addObject("url", "login")
                    .addObject("message", "发生未知错误，请重新登录！");
            return modelAndView;
        }
        User user = (User)result.get("user");
        modelAndView.setViewName("invoice_query_list");
        modelAndView.addObject("has_authority", user.getAuthority().getQueryInvoice())
                .addObject("view_invoice", false)
                .addObject("invoice", null)
                .addObject("invoice_list", new ArrayList<Invoice>())
                .addObject("has_result", false)
                .addObject("edit_invoice", false)
                .addObject("auth_message", null);
        return modelAndView;
    }

    @RequestMapping(value = "/queryForList", method = RequestMethod.POST)
    public ModelAndView queryForList(@SessionAttribute(SessionContext.ATTR_USER_ID) int userId,
                                     @SessionAttribute(SessionContext.ATTR_USER_NAME) String displayName,
                                     @RequestParam(name = "action", required = false, defaultValue = "") String action,
                                     @RequestParam(name = "preAction", required = false, defaultValue = "") String preAction,
                                     @RequestParam(name = "startDate", required = false, defaultValue = "") String startDate,
                                     @RequestParam(name = "endDate", required = false, defaultValue = "") String endDate,
                                     @RequestParam(name = "selfName", required = false, defaultValue = "") String self,
                                     @RequestParam(name = "itName", required = false, defaultValue = "") String it,
                                     @RequestParam(name = "id", required = false) Long id,
                                     @ModelAttribute Invoice editedInvoice) {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.addObject("display_name", displayName);
        Result result = userService.getUser(userId);
        if (!result.isSuccessful()) {
            modelAndView.setViewName("tip");
            modelAndView.addObject("url", "login")
                    .addObject("message", "发生未知错误，请重新登录！");
            return modelAndView;
        }
        User user = (User)result.get("user");
        modelAndView.setViewName("invoice_query_list");
        if ("queryForList".equals(preAction) && "query".equals(action)) {
            Result queryResult;
            List<Invoice> invoices;
            try {
                queryResult = invoiceService.searchInvoice(userId,
                        new Date(dateFormat.parse(startDate).getTime()),
                        new Date(dateFormat.parse(endDate).getTime()), self, it);
                if (queryResult.isSuccessful()) {
                    invoices = invoiceService.getInvoiceList(userId);
                } else {
                    invoices = new ArrayList<>();
                }

            } catch (ParseException e) {
                invoices = new ArrayList<>();
            }
            modelAndView.addObject("has_authority", user.getAuthority().getQueryInvoice())
                    .addObject("view_invoice", false)
                    .addObject("invoice", null)
                    .addObject("invoice_list", invoices)
                    .addObject("has_result", invoices.size() != 0)
                    .addObject("edit_invoice", false)
                    .addObject("auth_message", null);
        } else if ("queryForList".equals(preAction) && "view".equals(action)) {
            Invoice invoice = (Invoice)invoiceService.getInvoice(userId, id).get("invoice");
            List<Invoice> invoices = invoiceService.getInvoiceList(userId);
            modelAndView.addObject("has_authority", user.getAuthority().getQueryInvoice())
                    .addObject("view_invoice", invoice != null)
                    .addObject("invoice", invoice)
                    .addObject("invoice_list", invoices)
                    .addObject("has_result", invoices.size() != 0)
                    .addObject("edit_invoice", false)
                    .addObject("auth_message", null);
        } else if ("queryForList".equals(preAction) && "delete".equals(action)) {
            if (user.getAuthority().getRemoveInvoice()) {
                invoiceService.deleteInvoice(id);
            }
            List<Invoice> invoices = invoiceService.getInvoiceList(userId);
            modelAndView.addObject("has_authority", user.getAuthority().getQueryInvoice())
                    .addObject("view_invoice", false)
                    .addObject("invoice", null)
                    .addObject("invoice_list", invoices)
                    .addObject("has_result", invoices.size() != 0)
                    .addObject("edit_invoice", false)
                    .addObject("auth_message", user.getAuthority().getRemoveInvoice() ? null : "您无权限删除发票，请联系管理员！");
        } else if ("queryForList".equals(preAction) && "edit".equals(action)){
            Invoice invoice;
            if (user.getAuthority().getModifyInvoice()) {
                invoice = (Invoice)invoiceService.getInvoice(userId, id).get("invoice");
            } else {
                invoice = null;
            }
            List<Invoice> invoices = invoiceService.getInvoiceList(userId);
            modelAndView.addObject("has_authority", user.getAuthority().getQueryInvoice())
                    .addObject("view_invoice", false)
                    .addObject("invoice", invoice)
                    .addObject("invoice_list", invoices)
                    .addObject("has_result", invoices.size() != 0)
                    .addObject("edit_invoice", invoice != null)
                    .addObject("has_errors", false)
                    .addObject("error_messages", null)
                    .addObject("detail_num", invoice != null ? invoice.getDetails().size() : 0)
                    .addObject("auth_message", user.getAuthority().getModifyInvoice() ? null : "您无权限编辑发票，请联系管理员！");
        } else if ("queryForList".equals(preAction) && "save".equals(action)) {
            List<Invoice> invoices = invoiceService.getInvoiceList(userId);
            if (user.getAuthority().getModifyInvoice()) {
                Result checkResult = invoiceService.checkInvoice(editedInvoice, false);
                if (checkResult.isSuccessful()) {
                    log.info(editedInvoice.toString());
                    invoiceService.modifyInvoice(editedInvoice);
                    modelAndView.addObject("has_authority", user.getAuthority().getQueryInvoice())
                            .addObject("view_invoice", false)
                            .addObject("invoice", null)
                            .addObject("invoice_list", invoices)
                            .addObject("has_result", invoices.size() != 0)
                            .addObject("edit_invoice", false)
                            .addObject("has_errors", false)
                            .addObject("error_messages", null)
                            .addObject("auth_message", null);
                } else {
                    modelAndView.addObject("has_authority", user.getAuthority().getQueryInvoice())
                            .addObject("view_invoice", false)
                            .addObject("invoice", editedInvoice)
                            .addObject("invoice_list", invoices)
                            .addObject("has_result", invoices.size() != 0)
                            .addObject("edit_invoice", true)
                            .addObject("has_errors", true)
                            .addObject("error_messages", checkResult.get("errorMessages"))
                            .addObject("auth_message", null);
                }
            } else {
                modelAndView.addObject("has_authority", user.getAuthority().getQueryInvoice())
                        .addObject("view_invoice", false)
                        .addObject("invoice", null)
                        .addObject("invoice_list", invoices)
                        .addObject("has_result", invoices.size() != 0)
                        .addObject("edit_invoice", false)
                        .addObject("has_errors", false)
                        .addObject("error_messages", null)
                        .addObject("auth_message", "您无权限编辑发票，请联系管理员！");
            }
        } else {
            return new ModelAndView("redirect:/main");
        }
        return modelAndView;
    }

    @RequestMapping(value = "report", method = RequestMethod.GET)
    public ModelAndView queryReportPage(@SessionAttribute(SessionContext.ATTR_USER_ID) int userId,
                                        @SessionAttribute(SessionContext.ATTR_USER_NAME) String displayName) {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.addObject("display_name", displayName);
        Result result = userService.getUser(userId);
        if (!result.isSuccessful()) {
            modelAndView.setViewName("tip");
            modelAndView.addObject("url", "login")
                    .addObject("message", "发生未知错误，请重新登录！");
            return modelAndView;
        }
        User user = (User)result.get("user");
        modelAndView.setViewName("invoice_report");
        modelAndView.addObject("has_result", false)
                .addObject("has_authority", user.getAuthority().getQueryReport());
        return modelAndView;
    }

    @SuppressWarnings("unchecked")
    @RequestMapping(value = "report", method = RequestMethod.POST)
    public ModelAndView queryReport(@SessionAttribute(SessionContext.ATTR_USER_ID) int userId,
                                    @SessionAttribute(SessionContext.ATTR_USER_NAME) String displayName,
                                    @RequestParam(name = "action", required = false, defaultValue = "") String action,
                                    @RequestParam(name = "preAction", required = false, defaultValue = "") String preAction,
                                    @RequestParam(name = "startDate", required = false, defaultValue = "") String startDate,
                                    @RequestParam(name = "endDate", required = false, defaultValue = "") String endDate,
                                    @RequestParam(name = "selfName", required = false, defaultValue = "") String self,
                                    @RequestParam(name = "itName", required = false, defaultValue = "") String it,
                                    @RequestParam(name = "pattern", required = false, defaultValue = "") String pattern) {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.addObject("display_name", displayName);
        Result result = userService.getUser(userId);
        if (!result.isSuccessful()) {
            modelAndView.setViewName("tip");
            modelAndView.addObject("url", "login")
                    .addObject("message", "发生未知错误，请重新登录！");
            return modelAndView;
        }
        User user = (User)result.get("user");
        modelAndView.setViewName("invoice_report");
        if (!user.getAuthority().getQueryReport()) {
            modelAndView.addObject("has_authority", false);
        } else if ("report".equals(preAction) && "query".equals(action)){
            Result queryResult, analysisResult;
            modelAndView.addObject("has_authority", true);
            try {
                Date start = new Date(dateFormat.parse(startDate).getTime());
                Date end = new Date(dateFormat.parse(endDate).getTime());
                queryResult = invoiceService.searchInvoice(userId, start, end, self, it);
                if (queryResult.isSuccessful() && invoiceService.getInvoiceList(userId).size() != 0) {
                    analysisResult = invoiceService.analyzeForReport(pattern,
                            (List<Invoice>)queryResult.get("incomeInvoices"),
                            (List<Invoice>)queryResult.get("outcomeInvoices"), start, end);
                    if (analysisResult.isSuccessful()) {    // 分析成功
                        modelAndView.addObject("balances", analysisResult.get("balances"))
                                .addObject("income_product_totals", analysisResult.get("income_product_totals"))
                                .addObject("outcome_product_totals", analysisResult.get("outcome_product_totals"))
                                .addObject("income_names", analysisResult.get("income_names"))
                                .addObject("outcome_names", analysisResult.get("outcome_names"))
                                .addObject("income_amounts", analysisResult.get("income_amounts"))
                                .addObject("outcome_amounts", analysisResult.get("outcome_amounts"))
                                .addObject("dates", analysisResult.get("dates"))
                                .addObject("incomes", analysisResult.get("incomes"))
                                .addObject("outcomes", analysisResult.get("outcomes"))
                                .addObject("income_comments", analysisResult.get("income_comments"))
                                .addObject("outcome_comments", analysisResult.get("outcome_comments"))
                                .addObject("compare_comments", analysisResult.get("compare_comments"))
                                .addObject("has_result", true);
                    } else {
                        modelAndView.addObject("has_result", false);
                    }
                } else {
                    modelAndView.addObject("has_result", false);
                }
            } catch (ParseException e) {
                modelAndView.addObject("has_result", false);
            }
        } else {
            return new ModelAndView("redirect:/main");
        }
        return modelAndView;
    }
}
