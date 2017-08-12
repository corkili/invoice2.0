package org.hld.invoice.dao.impl;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hld.invoice.dao.RecordDao;
import org.hld.invoice.entity.Record;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by 李浩然 On 2017/8/10.
 */
@Repository
@Transactional
public class RecordDaoImpl extends BaseDaoImpl<Record, Long> implements RecordDao {
    public long getTotalCount() {
        try {
            return ((Number)getCurrentSession().createQuery(" select count(r) from Record r ").uniqueResult()).longValue();
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }

    @Override
    @SuppressWarnings("unchecked")
    public List<Record> findRecords(boolean fuzzy, Date start, Date end, String... params) {
        Session session = getCurrentSession();
        if (params.length % 2 == 1) {
            throw new IllegalArgumentException("The number of params should be even");
        }
        String hql = " select r from Record r ";
        StringBuilder where = new StringBuilder();
        for (int j = 0; j < params.length; j += 2) {
            if (where.length() != 0) {
                where.append(" and ");
            }
            where.append(" r." + params[j]);
            if (fuzzy) {
                where.append(" like :").append(params[j]);
            } else {
                where.append(" = :").append(params[j]);
            }
        }
        if (start != null && end != null) {
            if (where.length() != 0) {
                where.append(" and ");
            }
            where.append(" (r.time between :startDate and :endDate) ");
        }
        if (where.length() != 0) {
            hql += " where " + where;
        }
        Query query = session.createQuery(hql);
        for (int j = 0; j < params.length; j += 2) {
            query.setParameter(params[j], params[j + 1]);
        }
        if (start != null && end != null) {
            query.setParameter("startDate", start).setParameter("endDate", end);
        }
        try {
            return query.list();
        } catch (Exception e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }
}
