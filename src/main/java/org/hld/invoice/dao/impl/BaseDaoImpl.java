package org.hld.invoice.dao.impl;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hld.invoice.dao.BaseDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.io.Serializable;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

/**
 * Created by 李浩然 On 2017/8/9.
 */
@Repository
public abstract class BaseDaoImpl<T, PK extends Serializable> implements BaseDao<T, PK> {

    private Class<T> entityClass;

    private SessionFactory sessionFactory;

    @Autowired
    public void setSessionFactory(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    Session getCurrentSession() {
        return sessionFactory.getCurrentSession();
    }

    @SuppressWarnings("unchecked")
    public BaseDaoImpl() {
        Type genType = getClass().getGenericSuperclass();
        Type[] params = ((ParameterizedType) genType).getActualTypeArguments();
        entityClass = (Class) params[0];
    }

    public void persist(T entity) {
        Session session = getCurrentSession();
        Transaction transaction = session.beginTransaction();
        try {
            session.persist(entity);
            transaction.commit();
        } catch (Exception e) {
            transaction.rollback();
        }
    }

    @SuppressWarnings("unchecked")
    public PK save(T entity) {
        Session session = getCurrentSession();
        Transaction transaction = session.beginTransaction();
        PK id = null;
        try {
            id = (PK)session.save(entity);
            transaction.commit();
        } catch (Exception e) {
            transaction.rollback();
        }
        return id;
    }

    @SuppressWarnings("unchecked")
    public T get(PK id) {
        Session session = getCurrentSession();
        Transaction transaction = session.beginTransaction();
        T entity = null;
        try {
            entity = (T)session.get(entityClass, id);
            transaction.commit();
        } catch (Exception e) {
            transaction.rollback();
        }
        return entity;
    }

    public abstract long getTotalCount();

    public void saveOrUpdate(T entity) {
        Session session = getCurrentSession();
        Transaction transaction = session.beginTransaction();
        try {
            session.saveOrUpdate(entity);
            transaction.commit();
        } catch (Exception e) {
            transaction.rollback();
        }
    }

    public void update(T entity) {
        Session session = getCurrentSession();
        Transaction transaction = session.beginTransaction();
        try {
            session.update(entity);
            transaction.commit();
        } catch (Exception e) {
            transaction.rollback();
        }
    }

    @SuppressWarnings("unchecked")
    public T merge(T entity) {
        Session session = getCurrentSession();
        Transaction transaction = session.beginTransaction();
        T result = null;
        try {
            result = (T)session.merge(entity);
            transaction.commit();
        } catch (Exception e) {
            transaction.rollback();
        }
        return result;
    }

    public void delete(T entity) {
        Session session = getCurrentSession();
        Transaction transaction = session.beginTransaction();
        try {
            session.delete(entity);
            transaction.commit();
        } catch (Exception e) {
            transaction.rollback();
        }
    }

    @SuppressWarnings("unchecked")
    public void delete(PK id) {
        Session session = getCurrentSession();
        Transaction transaction = session.beginTransaction();
        try {
            session.delete(session.load(entityClass, id));
            transaction.commit();
        } catch (Exception e) {
            transaction.rollback();
        }
    }
}
