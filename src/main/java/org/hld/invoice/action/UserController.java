package org.hld.invoice.action;

import lombok.extern.log4j.Log4j;
import org.hibernate.Session;
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
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.sql.Blob;
import java.util.*;
import java.util.regex.Pattern;

@Controller
@Log4j
public class UserController {

    private UserService userService;

    private InvoiceService invoiceService;

    private RecordService  recordService;

    @RequestMapping(value = "/init")
    public String initAdmin(HttpServletRequest request) {
        userService.initAdmin(request);
        return "redirect:/login";
    }

    @RequestMapping(value = "/no_login")
    public ModelAndView noLogin() {
        ModelAndView modelAndView = new ModelAndView("tip");
        modelAndView.addObject("url", "login")
                .addObject("message", "请登录后使用！");
        return modelAndView;
    }

    @RequestMapping(value = "/captcha", method = RequestMethod.GET)
    @ResponseBody
    public void captcha(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        CaptchaUtil.outputCaptcha(request, response);
    }

    @RequestMapping(value = "/headImage", method = RequestMethod.GET)
    @ResponseBody
    public void displayHeadImage(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        userService.outputHeadImage(Integer.valueOf(request.getSession().getAttribute(SessionContext.ATTR_USER_ID).toString()), response);
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
        if (session.getAttribute("validate") != null) {
            session.removeAttribute("validate");
        }
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
        String errorMessage;
        if (session.getAttribute("randomString") == null ||
                !session.getAttribute("randomString").toString().toLowerCase().equals(captcha.toLowerCase())) {
            modelAndView.setViewName("login");
            errorMessage = "验证码错误！";
        } else {
            Result result = userService.login(email, password, session);
            if (result.isSuccessful()) {
                modelAndView.setViewName("redirect:/main");
                return modelAndView;
            } else {
                errorMessage = result.getMessage();
                password = "";
                modelAndView.setViewName("login");
            }
        }
        modelAndView.addObject("has_error", true)
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
        return modelAndView.addObject("has_error", false)
                .addObject("error_message", "");
    }

    @RequestMapping(value = "/register", method = RequestMethod.POST)
    public ModelAndView register(@ModelAttribute("user") User user,
                                 @RequestParam("action") String action,
                                 @RequestParam("captcha") String captcha,
                                 @SessionAttribute("randomString") String random,
                                 HttpServletRequest request) {
        ModelAndView modelAndView = new ModelAndView();
        String errorMessage;
        if (!"register".equals(action)) {
            errorMessage = "不正确的请求！";
        } else if (random == null || !random.toLowerCase().equals(captcha.toLowerCase())) {
            errorMessage = "验证码错误！";
            modelAndView.setViewName("register");
        } else {
            Result registerResult = userService.register(user, request);
            if (!registerResult.isSuccessful()) {
                errorMessage = registerResult.getMessage();
                modelAndView.setViewName("register");
            } else {
                modelAndView.setViewName("tip");
                modelAndView.addObject("message", "注册成功，请登录邮箱激活账户！")
                        .addObject("url", "login");
                return modelAndView;
            }
        }
        return modelAndView.addObject("has_error", true)
                .addObject("error_message", errorMessage)
                .addObject("user", user);
    }

    @RequestMapping(value = "/verify", method = RequestMethod.GET)
    public ModelAndView verify(@RequestParam("action") String action,
                               @RequestParam("email") String email,
                               @RequestParam("code") String code,
                               HttpSession session) {
        Result verifyResult = userService.verifyEmailCode(email, code);
        ModelAndView modelAndView = new ModelAndView();
        if ("active".equals(action) && verifyResult.isSuccessful()) {
            Result activeResult = userService.activeUser(email);
            modelAndView.setViewName("tip");
            modelAndView.addObject("message", "邮箱：" + email +
                    "---" + (activeResult.isSuccessful() ? "激活成功！" : "激活失败！"))
                    .addObject("url", "login");
        } else if ("pwd".equals(action) && verifyResult.isSuccessful()) {
            modelAndView.setViewName("pwd");
            modelAndView.addObject("email", email)
                    .addObject("has_error", false)
                    .addObject("error_message", "");
            session.setAttribute("validate", HashUtil.generate(email));

        } else {
            modelAndView.setViewName("tip");
            modelAndView.addObject("message", verifyResult.getMessage())
                    .addObject("url", "login");
        }
        return modelAndView;
    }

    @RequestMapping(value = "/mail", method = RequestMethod.GET)
    public ModelAndView mailPage(@RequestParam("action") String action) {
        ModelAndView modelAndView = new ModelAndView("mail");
        if ("active".equals(action)) {
            modelAndView.addObject("title", "激活账户");
        } else {
            modelAndView.addObject("title", "重置密码");
        }
        modelAndView.addObject("action", action)
                .addObject("has_error", false)
                .addObject("error_message", "");
        return modelAndView;
    }

    @RequestMapping(value = "/mail", method = RequestMethod.POST)
    public ModelAndView sendEmail(@RequestParam("email") String email,
                                  @RequestParam("action") String action,
                                  @RequestParam("captcha") String captcha,
                                  @SessionAttribute("randomString") String random,
                                  HttpServletRequest request) {
        ModelAndView modelAndView = new ModelAndView();
        String errorMessage;
        if (!captcha.toLowerCase().equals(random.toLowerCase())) {
            errorMessage = "验证码错误";
            modelAndView.setViewName("mail");
        } else {
            String address = request.getScheme() + "://"
                    + request.getServerName() + ":"
                    + request.getServerPort() + request.getContextPath()
                    + "/verify";
            Result emailResult = userService.sendEmail(address, email, action, request.getSession());
            if (emailResult.isSuccessful()) {
                modelAndView.setViewName("tip");
                modelAndView.addObject("message", "邮件已成功发送，请登录邮箱查看！")
                        .addObject("url", "login");
                return modelAndView;
            } else {
                modelAndView.setViewName("mail");
                errorMessage = emailResult.getMessage();
            }
        }
        return modelAndView.addObject("has_error", true)
                .addObject("error_message", errorMessage);
    }


    @RequestMapping(value = "/pwd", method = RequestMethod.POST)
    public ModelAndView modifyPassword(@RequestParam("email") String email,
                                       @RequestParam("password") String password,
                                       @RequestParam("captcha") String captcha,
                                       @SessionAttribute("randomString") String random,
                                       HttpSession session) {
        ModelAndView modelAndView = new ModelAndView();
        String errorMessage;
        if (session.getAttribute("validate") == null
                || HashUtil.verify(email, session.getAttribute("validate").toString())) {
            modelAndView.setViewName("tip");
            modelAndView.addObject("message", "非法操作，未经授权！")
                    .addObject("url", "login");
            return modelAndView;
        } else if (!captcha.toLowerCase().equals(random.toLowerCase())) {
            errorMessage = "验证码错误";
            modelAndView.setViewName("pwd");
        } else {
            Result result = userService.modifyPassword(email, password);
            if (result.isSuccessful()) {
                modelAndView.setViewName("tip");
                modelAndView.addObject("message", "修改密码成功，请重新登录！")
                        .addObject("url", "login");
                session.removeAttribute("validate");
                return modelAndView;
            } else {
                modelAndView.setViewName("pwd");
                errorMessage = result.getMessage();
            }
        }
        return modelAndView.addObject("has_error", true)
                .addObject("error_message", errorMessage);

    }

    @RequestMapping(value = "/info", method = RequestMethod.GET)
    public ModelAndView modifyInformation(@SessionAttribute(SessionContext.ATTR_USER_ID) int userId,
                                          @SessionAttribute(SessionContext.ATTR_USER_NAME) String displayName) {
        ModelAndView modelAndView = new ModelAndView();
        Result result = userService.getUser(userId);
        if (!result.isSuccessful()) {
            modelAndView.setViewName("tip");
            modelAndView.addObject("url", "login")
                    .addObject("message", "发生未知错误，请重新登录！");
            return modelAndView;
        }
        User user = (User)result.get("user");
        modelAndView.setViewName("user_info");
        modelAndView.addObject("has_error", false)
                .addObject("image_error", false)
                .addObject("error_message", "")
                .addObject("display_name", displayName)
                .addObject("user", user);
        return modelAndView;
    }

    @RequestMapping(value = "/modifyInformation", method = RequestMethod.POST)
    public ModelAndView modifyInformation(@SessionAttribute(SessionContext.ATTR_USER_ID) int userId,
                                          @SessionAttribute(SessionContext.ATTR_USER_NAME) String displayName,
                                          @RequestParam("name") String name,
                                          @RequestParam("jobId") String jobId,
                                          @RequestParam("phone") String phone) {
        ModelAndView modelAndView = new ModelAndView();
        Result result = userService.getUser(userId);
        String errorMessage;
        if (!result.isSuccessful()) {
            modelAndView.setViewName("tip");
            modelAndView.addObject("url", "login")
                    .addObject("message", "发生未知错误，请重新登录！");
            return modelAndView;
        }
        User user = (User)result.get("user");
        if (StringUtils.isEmpty(name)) {
            errorMessage = "姓名不能为空";
        } else if (StringUtils.isEmpty(jobId)) {
            errorMessage = "职工号不能为空";
        } else if (StringUtils.isEmpty(phone)
                || !Pattern.compile("^((13[0-9])|(15[^4])|(18[0-9])|(17[0-9])|(147))\\d{8}$")
                .matcher(phone).matches()) {
            errorMessage = "手机号格式不正确";
        } else {
            user.setName(name);
            user.setJobId(jobId);
            user.setPhone(phone);
            userService.modifyUserInformation(user);
            modelAndView.setViewName("redirect:/main");
            return modelAndView;
        }
        modelAndView.setViewName("user_info");
        modelAndView.addObject("has_error", true)
                .addObject("image_error", false)
                .addObject("error_message", errorMessage)
                .addObject("display_name", displayName)
                .addObject("user", user);
        return modelAndView;
    }

    @RequestMapping(value = "/modifyHeadImage", method = RequestMethod.POST)
    public ModelAndView modifyHeadImage(@RequestParam("image") MultipartFile file, HttpServletRequest request,
                                        @SessionAttribute(SessionContext.ATTR_USER_ID) int userId,
                                        @SessionAttribute(SessionContext.ATTR_USER_NAME) String displayName) {
        ModelAndView modelAndView = new ModelAndView();
        Result result = userService.getUser(userId);
        if (!result.isSuccessful()) {
            modelAndView.setViewName("tip");
            modelAndView.addObject("url", "login")
                    .addObject("message", "发生未知错误，请重新登录！");
            return modelAndView;
        }
        User user = (User)result.get("user");
        Result blobResult = userService.file2Blob(request, file);
        if (blobResult.isSuccessful()) {
            user.setImage((Blob)blobResult.get("blob"));
            userService.modifyUserInformation(user);
            modelAndView.setViewName("redirect:/main");
            return modelAndView;
        }
        modelAndView.setViewName("user_info");
        modelAndView.addObject("has_error", false)
                .addObject("image_error", true)
                .addObject("error_message", blobResult.getMessage())
                .addObject("display_name", displayName)
                .addObject("user", user);
        return modelAndView;
    }


    @SuppressWarnings("unchecked")
    @RequestMapping(value = "/manage", method = RequestMethod.GET)
    public ModelAndView manageUser(@SessionAttribute(SessionContext.ATTR_USER_ID) int userId,
                                   @SessionAttribute(SessionContext.ATTR_USER_NAME) String displayName) {
        ModelAndView modelAndView = new ModelAndView();
        Result result = userService.getUser(userId);
        if (!result.isSuccessful()) {
            modelAndView.setViewName("tip");
            modelAndView.addObject("url", "login")
                    .addObject("message", "发生未知错误，请重新登录！");
            return modelAndView;
        }
        User user = (User)result.get("user");
        modelAndView.setViewName("manage");
        if (!user.getIsSuperManager() && !user.getIsManager()) {
            modelAndView.addObject("has_authority", false)
                    .addObject("display_name", displayName);

            return modelAndView;
        }
        List<User> users = (List<User>)userService.getUsers(false, false).get("users");
        if (user.getIsSuperManager()) {
            users.addAll((List<User>)userService.getUsers(false, true).get("users"));
        } else {
            users.add(user);
        }
        modelAndView.addObject("has_authority", true)
                .addObject("user", user)
                .addObject("user_list", users)
                .addObject("display_name", displayName);
        return modelAndView;
    }

    @SuppressWarnings("unchecked")
    @RequestMapping(value = "/manage", method = RequestMethod.POST)
    public ModelAndView modifyAuth(HttpServletRequest request,
                                   @SessionAttribute(SessionContext.ATTR_USER_ID) int userId,
                                   @SessionAttribute(SessionContext.ATTR_USER_NAME) String displayName) {
        ModelAndView modelAndView = new ModelAndView();
        Result result = userService.getUser(userId);
        if (!result.isSuccessful()) {
            modelAndView.setViewName("tip");
            modelAndView.addObject("url", "login")
                    .addObject("message", "发生未知错误，请重新登录！");
            return modelAndView;
        }
        User user = (User)result.get("user");
        modelAndView.setViewName("manage");
        if (!user.getIsSuperManager() && !user.getIsManager()) {
            modelAndView.addObject("has_authority", false)
                    .addObject("display_name", displayName);
            return modelAndView;
        }
        List<User> userList = new ArrayList<>();
        String[] userIds = request.getParameterValues("table_records");
        List<String> authorities = Arrays.asList(request.getParameterValues("authority"));
        for (String id : userIds) {
            userList.add((User)userService.getUser(Integer.parseInt(id)).get("user"));
        }
        boolean addInvoice = authorities.contains("addInvoice");
        boolean queryInvoice = authorities.contains("queryInvoice");
        boolean modifyInvoice = authorities.contains("modifyInvoice");
        boolean removeInvoice = authorities.contains("removeInvoice");
        boolean queryReport = authorities.contains("queryReport");
        boolean queryRecord = authorities.contains("queryRecord");
        boolean isManager = authorities.contains("manager");

        for (User u : userList) {
            u.getAuthority().setAddInvoice(addInvoice);
            u.getAuthority().setQueryInvoice(queryInvoice);
            u.getAuthority().setModifyInvoice(modifyInvoice);
            u.getAuthority().setRemoveInvoice(removeInvoice);
            u.getAuthority().setQueryReport(queryReport);
            u.getAuthority().setQueryRecord(queryRecord);
            if (user.getIsSuperManager()) {
                u.setIsManager(isManager);
            }
        }
        userService.modifyUsersInformation(userList);
        List<User> users = (List<User>)userService.getUsers(false, false).get("users");
        if (user.getIsSuperManager()) {
            users.addAll((List<User>)userService.getUsers(false, true).get("users"));
        } else {
            users.add(user);
        }
        modelAndView.addObject("has_authority", true)
                .addObject("user", user)
                .addObject("user_list", users)
                .addObject("display_name", displayName);
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
