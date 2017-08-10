package org.hld.invoice.service.impl;

import lombok.Getter;
import lombok.extern.log4j.Log4j;
import org.hld.invoice.common.model.Result;
import org.hld.invoice.common.session.SessionContext;
import org.hld.invoice.dao.UserDao;
import org.hld.invoice.entity.User;
import org.hld.invoice.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


import javax.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by 李浩然 On 2017/8/10.
 */
@Service
@Log4j
public class UserServiceImpl implements UserService {

    private UserDao userDao;

    private SessionContext sessionContext;

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
        return null;
    }

    @Override
    public Result logout(int userId, HttpSession session) {
        return null;
    }

    @Override
    public Result register(User user, HttpSession session) {
        return null;
    }

    @Override
    public Result getUser(int userId) {
        return null;
    }

    @Override
    public Result modifyUserInformation(User user) {
        return null;
    }

    @Override
    public Result sendEmail(String email, String action, HttpSession session) {
        return null;
    }

    @Override
    public Result verifyEmailCode(String email, String code) {
        return null;
    }

    @Override
    public Result modifyPassword(String email, String password) {
        return null;
    }

    @Override
    public Result activeUser(String email) {
        return null;
    }

    private class UserContext {

        private Map<Integer, User> loginUsers;

        private UserContext() {
            loginUsers = new HashMap<Integer, User>();
        }

        public void login(User user) {
            loginUsers.put(user.getId(), user);
        }

        public void logout(User user) {
            loginUsers.remove(user.getId());
        }

        public void update(Integer... id) {
            for (Integer i : id) {
                if (i != null && loginUsers.containsKey(i)) {
                    loginUsers.put(i, userDao.get(i));
                }
            }
        }

        public void update(List<Integer> id) {
            for (Integer i : id) {
                if (i != null && loginUsers.containsKey(i)) {
                    loginUsers.put(i, userDao.get(i));
                }
            }
        }
    }

}
