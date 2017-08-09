package org.hld.invoice.dao;

import java.io.Serializable;
import java.util.List;

/**
 * Created by 李浩然 On 2017/8/9.
 */
public interface BaseDao<T, PK extends Serializable> {
    void persist(T entity);

    PK save(T entity);

    T get(PK id);

    long getTotalCount();

    void saveOrUpdate(T entity);

    void update(T entity);

    T merge(T entity);

    void delete(T entity);

    void delete(PK id);
}
