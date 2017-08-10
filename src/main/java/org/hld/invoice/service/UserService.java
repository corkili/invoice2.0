package org.hld.invoice.service;


import org.hld.invoice.common.model.Result;
import org.hld.invoice.entity.User;

import javax.servlet.http.HttpSession;

/**
 * Created by 李浩然 On 2017/8/10.
 */
public interface UserService {
    Result login(String email, String password, HttpSession session);

    Result logout(int userId, HttpSession session);

    Result register(User user, HttpSession session);

    Result getUser(int userId);

    Result modifyUserInformation(User user);

    Result sendEmail(String email, String action, HttpSession session);

    Result verifyEmailCode(String email, String code);

    Result modifyPassword(String email, String password);

    Result activeUser(String email);
}
