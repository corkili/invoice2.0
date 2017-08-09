package org.hld.invoice.dao;

import org.hld.invoice.entity.User;

import java.util.List;

/**
 * Created by 李浩然 On 2017/8/9.
 */
public interface UserDao extends BaseDao<User, Integer> {
    List<User> findAllUser();

    User findUserByEmail(String email);
}
