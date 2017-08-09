package org.hld.invoice.dao.impl;

import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hld.invoice.dao.UserDao;
import org.hld.invoice.entity.User;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by 李浩然 On 2017/8/9.
 */
@Repository
public class UserDaoImpl extends BaseDaoImpl<User, Integer> implements UserDao {

    @Override
    public long getTotalCount() {
        Session session = getCurrentSession();
        Transaction transaction = session.beginTransaction();
        Number count = 0;
        try {
            count = (Number)session.createQuery(" select count(u) from User u ").uniqueResult();
            transaction.commit();
        } catch (Exception e) {
            transaction.rollback();
        }
        return count.longValue();
    }

    @SuppressWarnings("unchecked")
    public List<User> findAllUser() {
        Session session = getCurrentSession();
        Transaction transaction = session.beginTransaction();
        List<User> users = new ArrayList<User>();
        try {
            users = session.createQuery(" from User ").list();
            transaction.commit();
        } catch (Exception e) {
            transaction.rollback();
        }
        return users;
    }

    @SuppressWarnings("unchecked")
    public User findUserByEmail(String email) {
        Session session = getCurrentSession();
        Transaction transaction = session.beginTransaction();
        User user = null;
        try {
            user = (User)session.createQuery(" from User u where u.email = :email")
                    .setParameter("email", email).uniqueResult();
            transaction.commit();
        } catch (Exception e) {
            transaction.rollback();
        }
        return user;
    }
}
