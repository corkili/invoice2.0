package org.hld.invoice.dao.impl;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hld.invoice.dao.BaseDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.io.Serializable;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by 李浩然 On 2017/8/9.
 */
@Repository
@Transactional
public abstract class BaseDaoImpl<T, PK extends Serializable> implements BaseDao<T, PK> {

    private Class<T> entityClass;

    private SessionFactory sessionFactory;

    @Autowired
    public void setSessionFactory(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    public Session getCurrentSession() {
        return sessionFactory.getCurrentSession();
    }

    @SuppressWarnings("unchecked")
    public BaseDaoImpl() {
        Type genType = getClass().getGenericSuperclass();
        Type[] params = ((ParameterizedType) genType).getActualTypeArguments();
        entityClass = (Class) params[0];
    }

    public void persist(T entity) {
        try {
            getCurrentSession().persist(entity);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @SuppressWarnings("unchecked")
    public PK save(T entity) {
        try {
            return (PK)getCurrentSession().save(entity);
        } catch (Exception e) {
            return null;
        }
    }

    @SuppressWarnings("unchecked")
    public List<T> batchSave(List<T> entities) {
        List<T> errorEntities = new ArrayList<>();
        Session session = sessionFactory.openSession();
        Transaction transaction = session.beginTransaction();
        try {
            for (int i = 0; i < entities.size(); i++) {
                if (session.save(entities.get(i)) == null) {
                    errorEntities.add(entities.get(i));
                }
                if (i % 10 == 0) {
                    session.flush();
                    session.clear();
                }
            }
            transaction.commit();
        } catch (Exception e) {
            transaction.rollback();
        } finally {
            session.close();
        }
        entities.removeAll(errorEntities);
        return errorEntities;
    }

    @SuppressWarnings("unchecked")
    public T get(PK id) {
        try {
            return (T)getCurrentSession().get(entityClass, id);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public abstract long getTotalCount();

    public void saveOrUpdate(T entity) {
        try {
            getCurrentSession().saveOrUpdate(entity);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void update(T entity) {
        try {
            getCurrentSession().update(entity);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @SuppressWarnings("unchecked")
    public T merge(T entity) {
        try {
            return (T)getCurrentSession().merge(entity);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public void delete(T entity) {
        try {
            getCurrentSession().delete(entity);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @SuppressWarnings("unchecked")
    public void delete(PK id) {
        try {
            Session session = getCurrentSession();
            session.delete(session.load(entityClass, id));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
