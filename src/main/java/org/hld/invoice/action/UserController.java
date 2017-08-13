package org.hld.invoice.action;

import lombok.extern.log4j.Log4j;
import org.hld.invoice.common.model.Result;
import org.hld.invoice.common.session.SessionContext;
import org.hld.invoice.common.utils.CaptchaUtil;
import org.hld.invoice.common.utils.HashUtil;
import org.hld.invoice.entity.Authority;
import org.hld.invoice.entity.User;
import org.hld.invoice.service.InvoiceService;
import org.hld.invoice.service.RecordService;
import org.hld.invoice.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.sql.Blob;
import java.util.Base64;
import java.util.Date;

@Controller
@Log4j
public class UserController {

    private UserService userService;

    private InvoiceService invoiceService;

    private RecordService  recordService;

    @RequestMapping(value = "/init")
    public String initAdmin(HttpServletRequest request) {
        User user = userService.getUserDao().findUserByEmail("admin@admin.com");
        if (user == null) {
            user = new User();
            user.setEmail("admin@admin.com");
            user.setPassword(HashUtil.generate("admin"));
            user.setName("管理员");
            user.setJobId("无");
            user.setPhone("无");
            user.setIsSuperManager(true);
            user.setIsManager(true);
            user.setEnabled(true);
            user.setVerificationCode(null);
            user.setCreateTime(new Date());
            user.setVerifyTime(null);
            user.setImage((Blob)userService.file2Blob(request, null).get("blob"));
            Authority authority = new Authority();
            authority.setQueryReport(true);
            authority.setRemoveInvoice(true);
            authority.setQueryInvoice(true);
            authority.setModifyInvoice(true);
            authority.setAddInvoice(true);
            authority.setQueryRecord(true);
            userService.getUserDao().save(user);
        }
        return "redirect:/login";
    }

    @RequestMapping(value = "/no_login")
    public String noLogin() {
        return "no_login";
    }

    @RequestMapping(value = "/captcha", method = RequestMethod.GET)
    @ResponseBody
    public void captcha(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        CaptchaUtil.outputCaptcha(request, response);
    }

    @RequestMapping(value = "/main", name = "主页", method = RequestMethod.GET)
    public ModelAndView mainPage(HttpSession session) {
        int userId = Integer.parseInt(session.getAttribute(SessionContext.ATTR_USER_ID).toString());
        String displayName = session.getAttribute(SessionContext.ATTR_USER_NAME).toString();
        User user = (User)userService.getUser(userId).get("user");
        return new ModelAndView("main")
                .addObject("user", user)
                .addObject("display_name", displayName)
                .addObject("has_message", false);
    }

    @RequestMapping(value = "/logout", name = "注销", method = {RequestMethod.GET, RequestMethod.POST})
    public String logout(HttpSession session) {
        int userId = Integer.parseInt(session.getAttribute(SessionContext.ATTR_USER_ID).toString());
        userService.logout(userId, session);
        invoiceService.onLogout(userId);
        return "redirect:/login";
    }

    @RequestMapping(value = "/login", method = RequestMethod.GET)
    public ModelAndView login(HttpSession session) {
        ModelAndView modelAndView = new ModelAndView();
        if (session.getAttribute(SessionContext.ATTR_USER_ID) != null) {
            int userId = Integer.parseInt(session.getAttribute(SessionContext.ATTR_USER_ID).toString());
            userService.logout(userId, session);
            invoiceService.onLogout(userId);
        }
        modelAndView.setViewName("login");
        modelAndView.addObject("has_error", false)
                .addObject("error_message", "")
                .addObject("email", "")
                .addObject("password", "");
        return modelAndView;
    }

    @RequestMapping(value = "/login", method = RequestMethod.POST)
    public ModelAndView login(HttpSession session, @RequestParam("email") String email,
                              @RequestParam("password") String password,
                              @RequestParam("captcha") String captcha) {
        ModelAndView modelAndView = new ModelAndView();
        boolean hasError;
        String errorMessage;
        if (session.getAttribute("randomString") == null ||
                !session.getAttribute("randomString").toString().toLowerCase().equals(captcha.toLowerCase())) {
            modelAndView.setViewName("login");
            hasError = true;
            errorMessage = "验证码错误！";
        } else {
            Result result = userService.login(email, password, session);
            if (result.isSuccessful()) {
                modelAndView.setViewName("redirect:/main");
                return modelAndView;
            } else {
                hasError = true;
                errorMessage = result.getMessage();
                password = "";
                modelAndView.setViewName("login");
            }
        }
        modelAndView.addObject("has_error", hasError)
                .addObject("error_message", errorMessage)
                .addObject("email", email)
                .addObject("password", password);
        return modelAndView;
    }

    @RequestMapping(value = "/register", method = RequestMethod.GET)
    public ModelAndView register(@ModelAttribute("user") User user) {
        ModelAndView modelAndView = new ModelAndView("register");
        if (user == null) {
            modelAndView.addObject("user", new User());
        } else {
            modelAndView.addObject("user", user);
        }
        return modelAndView;
    }

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
}
