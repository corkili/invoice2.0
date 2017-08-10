package org.hld.invoice.dao.impl;

import org.hld.invoice.dao.UserDao;
import org.hld.invoice.entity.User;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by 李浩然 On 2017/8/9.
 */
@Repository
@Transactional
public class UserDaoImpl extends BaseDaoImpl<User, Integer> implements UserDao {

    @Override
    public long getTotalCount() {
        try {
            return ((Number)getCurrentSession().createQuery(" select count(u) from User u ").uniqueResult()).longValue();
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }

    @SuppressWarnings("unchecked")
    public List<User> findAllUser() {
        try {
            return getCurrentSession().createQuery(" from User ").list();
        } catch (Exception e) {
            e.printStackTrace();
            return new ArrayList<User>();
        }
    }

    @SuppressWarnings("unchecked")
    public User findUserByEmail(String email) {
        try {
            return (User)getCurrentSession().createQuery(" from User u where u.email = :email")
                    .setParameter("email", email).uniqueResult();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
