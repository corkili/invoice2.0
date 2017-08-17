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
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

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


    @RequestMapping(value = "/addInvoice", method = RequestMethod.GET)
    public ModelAndView addInvoicePage(@SessionAttribute(SessionContext.ATTR_USER_ID) int userId,
                                       @SessionAttribute(SessionContext.ATTR_USER_NAME) String displayName,
                                       @RequestParam("action") String action, HttpServletRequest request) {
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
        switch (action) {
            case "hand":
                modelAndView.setViewName("invoice_input_hand");
                modelAndView.addObject("has_authority", user.getAuthority().getAddInvoice())
                        .addObject("detail_num", 0);
                break;
            case "image":
                modelAndView.setViewName("invoice_input_image");
                break;
            case "excel":
                modelAndView.setViewName("invoice_input_excel");
                break;
            case "result":
                modelAndView.setViewName("invoice_save_result");
                String id = request.getParameter("id");
                if (id != null) {
                    Invoice invoice = (Invoice)invoiceService.getInvoice(Integer.parseInt(id)).get("invoice");
                    modelAndView.addObject("has_result", invoice != null);
                    modelAndView.addObject("invoice", invoice);
                    modelAndView.addObject("nextAction", request.getParameter("preAction"));
                } else {
                    modelAndView.addObject("has_result", false);
                }
                break;
            default:
                modelAndView = new ModelAndView("redirect:/main");
        }
        return modelAndView;
    }


    @RequestMapping(value = "/addInvoice", method = RequestMethod.POST)
    public ModelAndView inputDetailNum(@SessionAttribute(SessionContext.ATTR_USER_ID) int userId,
                                       @SessionAttribute(SessionContext.ATTR_USER_NAME) String displayName,
                                       @RequestParam("action") String action, HttpServletRequest request,
                                       @RequestParam("preAction") String preAction,
                                       @ModelAttribute Invoice invoice) {
        ModelAndView modelAndView = new ModelAndView("invoice_input_hand");
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
                if (saveResult.isSuccessful()) {
                    invoice = (Invoice)saveResult.get("invoice");
                    modelAndView.setViewName("redirect:/addInvoice?preAction=hand&action=result&id=" + invoice.getId());
                } else {
                    modelAndView.setViewName("redirect:/addInvoice?action=result");
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
        }
        return modelAndView;
    }

}
