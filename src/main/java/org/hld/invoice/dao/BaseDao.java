package org.hld.invoice.dao;

import org.hibernate.Session;

import java.io.Serializable;
import java.util.List;

/**
 * Created by 李浩然 On 2017/8/9.
 */
@SuppressWarnings("unused")
public interface BaseDao<T, PK extends Serializable> {
    void persist(T entity);

    PK save(T entity);

    List<T> batchSave(List<T> entities);

    T get(PK id);

    long getTotalCount();

    void saveOrUpdate(T entity);

    void update(T entity);

    @SuppressWarnings("all")
    T merge(T entity);

    void delete(T entity);

    void delete(PK id);

    Session getCurrentSession();
}
