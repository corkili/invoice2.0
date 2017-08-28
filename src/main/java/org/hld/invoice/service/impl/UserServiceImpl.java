package org.hld.invoice.service.impl;

import lombok.Getter;
import lombok.extern.log4j.Log4j;
import org.apache.commons.io.IOUtils;
import org.hibernate.Hibernate;
import org.hld.invoice.common.model.Result;
import org.hld.invoice.common.session.SessionContext;
import org.hld.invoice.common.utils.EmailUtil;
import org.hld.invoice.common.utils.HashUtil;
import org.hld.invoice.common.utils.ImageUtil;
import org.hld.invoice.dao.UserDao;
import org.hld.invoice.entity.Authority;
import org.hld.invoice.entity.User;
import org.hld.invoice.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;


import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.awt.image.BufferedImage;
import java.io.*;
import java.sql.Blob;
import java.util.*;
import java.util.regex.Pattern;

/**
 * Created by 李浩然 On 2017/8/10.
 */
@Service
@Log4j
@Transactional
public class UserServiceImpl implements UserService {

    private UserDao userDao;

    private SessionContext sessionContext;

    private static final String CHECK_EMAIL = "^\\s*\\w+(?:\\.{0,1}[\\w-]+)*@[a-zA-Z0-9]+(?:[-.][a-zA-Z0-9]+)*\\.[a-zA-Z]+\\s*$";

    private static final String CHECK_PASSWORD = "^(?![0-9]+$)(?![a-zA-Z]+$)[0-9A-Za-z]{8,24}$";

    private static final String CHECK_PHONE = "^((13[0-9])|(15[^4])|(18[0-9])|(17[0-9])|(147))\\d{8}$";

    private static final long TIME_DIFF = 24 * 60 * 60 * 1000;

    @Getter
    private UserContext userContext;

    @Autowired
    public void setUserDao(UserDao userDao) {
        this.userDao = userDao;
    }

    public UserServiceImpl() {
        userContext = new UserContext();
        sessionContext = SessionContext.getInstance();
    }

    @Override
    public Result login(String email, String password, HttpSession session) {
        boolean successful = false;
        String message;
        User user = null;
        if (StringUtils.isEmpty(email)) {
            message = "邮箱地址不能为空！";
        } else if (StringUtils.isEmpty(password)){
            message = "密码不能为空！";
        } else {
            user = userDao.findUserByEmail(email);
            if (user == null) {
                message = "用户或密码错误！";
            } else if (!user.getEnabled()) {
                message = "账号未激活！";
            } else if (!HashUtil.verify(password, user.getPassword())) {
                message = "用户或密码错误！";
            } else {
                userContext.login(user);
                session.setAttribute(SessionContext.ATTR_USER_ID, String.valueOf(user.getId()));
                session.setAttribute(SessionContext.ATTR_USER_NAME, user.getName());
                sessionContext.sessionHandlerByCacheMap(session);
                successful = true;
                message = "登录成功！";
            }
        }
        Result result = new Result(successful);
        result.setMessage(message);
        result.add("user", user);
        return result;
    }

    @Override
    public void logout(int userId, HttpSession session) {
        userContext.logout(userId);
        session.invalidate();
    }

    @Override
    public Result register(User user, HttpServletRequest request) {
        boolean successful = false;
        String message;
        if (StringUtils.isEmpty(user.getEmail())
                || !Pattern.compile(CHECK_EMAIL).matcher(user.getEmail()).matches()) {
            message = "邮箱不能为空或邮箱格式不正确！";
        } else if (userDao.findUserByEmail(user.getEmail()) != null) {
            message = "邮箱已存在！";
        } else if (StringUtils.isEmpty(user.getPassword())
                || !Pattern.compile(CHECK_PASSWORD).matcher(user.getPassword()).matches()){
            message = "密码必须为包含字母和数字，且长度为8到24";
        } else if (StringUtils.isEmpty(user.getName())) {
            message = "姓名不能为空";
        } else if (StringUtils.isEmpty(user.getJobId())) {
            message = "职工号不能为空";
        } else if (StringUtils.isEmpty(user.getPhone())
                || !Pattern.compile(CHECK_PHONE).matcher(user.getPhone()).matches()) {
            message = "手机号格式不正确";
        } else {
            user.setIsSuperManager(false);
            user.setIsManager(false);
            user.setEnabled(false);
            user.setCreateTime(new Date());
            String password = user.getPassword();
            user.setPassword(HashUtil.generate(password));
            if (user.getImage() == null) {
                user.setImage((Blob)file2Blob(request, null).get("blob"));
            }
            user.setAuthority(new Authority());
            if (userDao.save(user) == null) {
                message = "数据库错误！";
            } else {
                String address = request.getScheme() + "://"
                        + request.getServerName() + ":"
                        + request.getServerPort() + request.getContextPath()
                        + "/verify";
                if (sendEmail(address, user.getEmail(), "active", request.getSession()).isSuccessful()) {
                    successful = true;
                    message = "注册成功！";
                } else {
                    message = "无法向指定邮箱发送邮件，请检查邮箱是否正确或检查邮箱设置！";
                    user.setPassword(password);
                    userDao.delete(user);
                    user.setId(null);
                }
            }
        }
        Result result = new Result(successful);
        result.setMessage(message);
        result.add("user", user);
        return result;
    }

    @Override
    public void initAdmin(HttpServletRequest request) {
        User user = userDao.findUserByEmail("admin@admin.com");
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
            user.setImage((Blob)file2Blob(request, null).get("blob"));
            Authority authority = new Authority();
            authority.setQueryReport(true);
            authority.setRemoveInvoice(true);
            authority.setQueryInvoice(true);
            authority.setModifyInvoice(true);
            authority.setAddInvoice(true);
            authority.setQueryRecord(true);
            user.setAuthority(authority);
            userDao.save(user);
        }
    }

    @Override
    public Result getUser(int userId) {
        boolean successful = false;
        String message;
        User user;
        if ((user = userContext.get(userId)) != null) {
            successful = true;
            message = "已获取用户" + userId + "！";
        } else if ((user = userDao.get(userId)) != null) {
            successful = true;
            message = "已获取用户" + userId + "！";
        } else {
            message = "不存在用户" + user + "！";
        }
        Result result = new Result(successful);
        result.setMessage(message);
        result.add("user", user);
        return result;
    }

    @Override
    public User getUser(String email) {
        return userDao.findUserByEmail(email);
    }

    @Override
    public Result getUsers(boolean isSuperManager, boolean isManager) {
        Result result = new Result(true);
        result.setMessage("获取用户成功！");
        result.add("users", userDao.findUsersByManager(isSuperManager, isManager));
        return result;
    }

    @Override
    public void modifyUserInformation(User user) {
        userDao.saveOrUpdate(user);
        userContext.update(user);
    }

    @Override
    public void modifyUsersInformation(List<User> users) {
        for (User user : users) {
            userDao.saveOrUpdate(user);
        }
        userContext.update(users);
    }

    @Override
    public Result sendEmail(String address, String email, String action, HttpSession session) {
        boolean successful = false;
        String message;
        User user = userDao.findUserByEmail(email);
        if (user == null) {
            message = "邮箱不存在！";
        } else if ("active".equals(action) && user.getEnabled()) {
            message = "账户已激活，请直接登录！";
        } else {
            String code = HashUtil.generate(email) + session.getId();
            user.setVerificationCode(code);
            user.setVerifyTime(new Date());
            userDao.saveOrUpdate(user);
            if (!EmailUtil.sendMail(email, address, code, action)) {
                message = "邮件发送失败！";
            } else {
                successful = true;
                message = "发送邮件至" + email + "成功！";
            }
        }
        Result result = new Result(successful);
        result.setMessage(message);
        return result;
    }

    @Override
    public Result verifyEmailCode(String email, String code) {
        boolean successful = false;
        String message;
        User user = userDao.findUserByEmail(email);
        if (user == null) {
            message = "链接验证失败，邮箱不存在！";
        } else {
            Date now = new Date();
            if (user.getVerifyTime() == null) {
                message = "链接已无效，请重新发送邮件！";
            } else if (now.getTime() - user.getVerifyTime().getTime() > TIME_DIFF) {
                message = "链接已超时，请重新发送邮件！";
            }
            else if (StringUtils.isEmpty(code) || StringUtils.isEmpty(user.getVerificationCode())
                    || !code.equals(user.getVerificationCode())){
                message = "链接已无效，请重新发送邮件！";
            } else {
                successful = true;
                message = "验证成功！";
            }
            user.setVerificationCode(null);
            user.setVerifyTime(null);
            userDao.saveOrUpdate(user);
        }
        Result result = new Result(successful);
        result.setMessage(message);
        result.add("user", user);
        return result;
    }

    @Override
    public Result modifyPassword(String email, String password) {
        boolean successful = false;
        String message;
        User user = userDao.findUserByEmail(email);
        if (user == null) {
            message = "邮箱不存在！";
        } else if (StringUtils.isEmpty(password)
                || !Pattern.compile(CHECK_PASSWORD).matcher(password).matches()) {
            message = "密码必须为包含字母和数字，且长度为8到24";
        } else {
                user.setPassword(HashUtil.generate(password));
                userDao.saveOrUpdate(user);
                successful = true;
                message = "修改密码成功";
        }
        Result result = new Result(successful);
        result.setMessage(message);
        result.add("user", user);
        return result;
    }

    @Override
    public Result activeUser(String email) {
        boolean successful = false;
        String message;
        User user = userDao.findUserByEmail(email);
        if (user == null) {
            message = "邮箱不存在！";
        } else {
            user.setEnabled(true);
            userDao.saveOrUpdate(user);
            successful = true;
            message = "激活成功";
        }
        Result result = new Result(successful);
        result.setMessage(message);
        return result;
    }

    @Override
    public void outputHeadImage(int id, HttpServletResponse response) {
        User user = (User)getUser(id).get("user");
        if (user != null) {
            BufferedImage bi = ImageUtil.BlobToImage(user.getImage());
            response.setContentType("image/png");
            if (bi != null) {
                try {
                    ImageIO.write(bi, "png", response.getOutputStream());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

    }

    @Override
    public Result file2Blob(HttpServletRequest request, MultipartFile file) {
        boolean successful = false;
        String message = "";
        Blob blob = null;
        if (file != null) {
            try {
                String type = file.getContentType();
                String base64 = Base64.getEncoder().encodeToString(file.getBytes());
                if (!"image/png".equals(type) || base64 == null || !base64.startsWith("iVBORw0KG")) {
                    message = "文件类型不正确";
                } else {
                    blob = Hibernate.getLobCreator(userDao.getCurrentSession()).createBlob(
                            IOUtils.toByteArray(file.getInputStream()));
                    successful = true;
                    message = "转换成功！";
                }
            } catch (IOException e) {
                blob = null;
                successful = false;
                message = "转换失败！";
            }
        } else {
            String path = request.getSession().getServletContext().getRealPath("WEB-INF/images");
            String fileName = "defaultHeadImage.png";
            File defaultImage = new File(path, fileName);
            try {
                InputStream inputStream = new FileInputStream(defaultImage);
                blob = Hibernate.getLobCreator(userDao.getCurrentSession()).createBlob(
                        IOUtils.toByteArray(inputStream));
                successful = true;
                message = "获取默认头像成功！";
            } catch (IOException e) {
                blob = null;
                successful = false;
                message = "获取默认头像失败！";
            }
        }
        Result result = new Result(successful);
        result.setMessage(message);
        result.add("blob", blob);
        return result;
    }

    private class UserContext {

        private Map<Integer, User> loginUsers;

        private UserContext() {
            loginUsers = new HashMap<>();
        }

        public User get(int userId) {
            return loginUsers.get(userId);
        }

        public void login(User user) {
            loginUsers.put(user.getId(), user);
        }

        public void logout(int userId) {
            loginUsers.remove(userId);
        }

        public void update(User... users) {
            for (User user : users) {
                if (user != null && loginUsers.containsKey(user.getId())) {
                    loginUsers.put(user.getId(), user);
                }
            }
        }

        public void update(List<User> users) {
            for (User user : users) {
                if (user != null && loginUsers.containsKey(user.getId())) {
                    loginUsers.put(user.getId(), user);
                }
            }
        }
    }

}
