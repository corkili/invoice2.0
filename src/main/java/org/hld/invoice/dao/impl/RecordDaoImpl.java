package org.hld.invoice.dao.impl;

import org.hld.invoice.dao.RecordDao;
import org.hld.invoice.entity.Record;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
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

    @SuppressWarnings("unchecked")
    public List<Record> findAllRecords() {
        try {
            return getCurrentSession().createQuery(" from Record ").list();
        } catch (Exception e) {
            e.printStackTrace();
            return new ArrayList<Record>();
        }
    }
}
