package org.hld.invoice.dao.impl;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hld.invoice.dao.InvoiceDao;
import org.hld.invoice.entity.Invoice;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Date;
import java.util.*;

/**
 * Created by 李浩然 On 2017/8/9.
 */
@Repository
@Transactional
public class InvoiceDaoImpl extends BaseDaoImpl<Invoice, Long> implements InvoiceDao {

    @Override
    public long getTotalCount() {
        try {
            return ((Number)getCurrentSession().createQuery(" select count(i) from Invoice i ").uniqueResult()).longValue();
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }

    @SuppressWarnings("unchecked")
    public List<Invoice> findInvoices(boolean fuzzy, Date start, Date end, String... params) throws IllegalArgumentException {
        Session session = getCurrentSession();
        if (params.length % 2 == 1) {
            throw new IllegalArgumentException("The number of params should be even");
        }
        String hql = " select i from Invoice i ";
        StringBuilder where = new StringBuilder();
        for (int j = 0; j < params.length; j += 2) {
            if (where.length() != 0) {
                where.append(" and ");
            }
            where.append(" i.").append(params[j]);
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
            where.append(" (i.invoiceDate between :startDate and :endDate) ");
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

    @Override
    @SuppressWarnings("unchecked")
    public Map<String, String> findNames() {
        try {
            List<Object[]> buyerNames = getCurrentSession().createQuery(" select distinct i.buyerName, i.buyerId" +
                    " from Invoice i ").list();
            List<Object[]> sellerNames = getCurrentSession().createQuery(" select distinct i.sellerName, i.sellerId" +
                    " from Invoice i ").list();
            Map<String, String> result = new HashMap<>();
            for (Object[] buyer : buyerNames) {
                if (buyer.length == 2) {
                    result.put((String)buyer[0], (String)buyer[1]);
                }
            }
            for (Object[] seller : buyerNames) {
                if (seller.length == 2) {
                    result.put((String)seller[0], (String)seller[1]);
                }
            }
            return result;
        } catch (Exception e) {
            e.printStackTrace();
            return new HashMap<>();
        }
    }

}
