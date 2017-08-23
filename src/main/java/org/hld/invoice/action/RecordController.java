package org.hld.invoice.action;

import lombok.extern.java.Log;
import lombok.extern.log4j.Log4j;
import org.hld.invoice.common.model.Result;
import org.hld.invoice.common.session.SessionContext;
import org.hld.invoice.entity.Record;
import org.hld.invoice.entity.User;
import org.hld.invoice.service.InvoiceService;
import org.hld.invoice.service.RecordService;
import org.hld.invoice.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttribute;
import org.springframework.web.servlet.ModelAndView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@Controller
@Log4j
public class RecordController {
    private UserService userService;

    private InvoiceService invoiceService;

    private RecordService recordService;

    private SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");

    private final long ONE_DAY = 86400000;

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

    @RequestMapping(value = "record", method = RequestMethod.GET)
    public ModelAndView recordPage(@SessionAttribute(SessionContext.ATTR_USER_ID) int userId,
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
        modelAndView.setViewName("record");
        Date end = new Date();
        Date start = new Date(ONE_DAY);
        List<Record> recordList = recordService.searchRecords(false, start, end);
        modelAndView.addObject("has_authority", user.getAuthority().getQueryRecord())
                .addObject("records", recordList)
                .addObject("has_result", true);
        return modelAndView;
    }

    @RequestMapping(value = "record", method = RequestMethod.POST)
    public ModelAndView recordPage(@SessionAttribute(SessionContext.ATTR_USER_ID) int userId,
                                   @SessionAttribute(SessionContext.ATTR_USER_NAME) String displayName,
                                   @RequestParam(name = "startDate") String startDate,
                                   @RequestParam(name = "endDate") String endDate) {
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
        log.info(startDate + " ~ " + endDate);
        Date start;
        Date end;
        try {
            start = dateFormat.parse(startDate);
            end = dateFormat.parse(endDate);
        } catch (ParseException e) {
            return new ModelAndView("redirect:/record");
        }
        List<Record> recordList = recordService.searchRecords(false, start, end);
        modelAndView.addObject("has_authority", user.getAuthority().getQueryRecord())
                .addObject("records", recordList)
                .addObject("has_result", true);
        return modelAndView;
    }
}
