package org.hld.invoice.action;

import lombok.extern.java.Log;
import org.hld.invoice.common.model.Result;
import org.hld.invoice.common.session.SessionContext;
import org.hld.invoice.entity.Authority;
import org.hld.invoice.entity.Invoice;
import org.hld.invoice.entity.InvoiceDetail;
import org.hld.invoice.entity.User;
import org.hld.invoice.service.InvoiceService;
import org.hld.invoice.service.RecordService;
import org.hld.invoice.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

@Controller
@Log
public class InvoiceController {

    private UserService userService;

    private InvoiceService invoiceService;

    private RecordService recordService;

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
            int bytesRead = 0;
            byte[] buffer = new byte[5 * 1024];
            while ((bytesRead = bis.read(buffer)) != -1) {
                bos.write(buffer, 0, bytesRead);
            }
            bos.flush();
        } catch(Exception e){
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
            } catch (IOException e) {
            }
        }
    }


}
