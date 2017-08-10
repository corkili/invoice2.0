package org.hld.invoice.service.impl;

import lombok.Getter;
import lombok.extern.log4j.Log4j;
import org.hld.invoice.dao.UserDao;
import org.hld.invoice.entity.User;
import org.hld.invoice.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


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

    @Getter
    private UserContext userContext;

    @Autowired
    public void setUserDao(UserDao userDao) {
        this.userDao = userDao;
    }

    public UserServiceImpl() {
        userContext = new UserContext();
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
