package org.hld.invoice.dao.impl;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hld.invoice.dao.InvoiceDao;
import org.hld.invoice.entity.Invoice;
import org.springframework.stereotype.Repository;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by 李浩然 On 2017/8/9.
 */
@Repository
public class InvoiceDaoImpl extends BaseDaoImpl<Invoice, Long> implements InvoiceDao {

    @Override
    public long getTotalCount() {
        Session session = getCurrentSession();
        Transaction transaction = session.beginTransaction();
        Number count = 0;
        try {
            count = (Number)session.createQuery(" select count(i) from Invoice i ").uniqueResult();
            transaction.commit();
        } catch (Exception e) {
            transaction.rollback();
        }
        return count.longValue();
    }

    @SuppressWarnings("unchecked")
    public List<Invoice> findInvoices(boolean fuzzy, Date start, Date end, String... params) throws IllegalArgumentException {
        Session session = getCurrentSession();
        Transaction transaction = session.beginTransaction();
        List<Invoice> invoices = new ArrayList<Invoice>();
        if (params.length % 2 == 1) {
            throw new IllegalArgumentException("The number of params should be even");
        }
        try {
            String hql = " select i from Invoice i ";
            String where = "";
            for (int j = 0; j < params.length; j += 2) {
                if (where.length() != 0) {
                    where += " and ";
                }
                where += " i." + params[j];
                if (fuzzy) {
                    where += " like :" + params[j];
                } else {
                    where += " = :" + params[j];
                }
            }
            if (start != null && end != null) {
                if (where.length() != 0) {
                    where += " and ";
                }
                where += " (i.invoiceDate between :startDate and :endDate) ";
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
            invoices = query.list();
            transaction.commit();
        } catch (Exception e) {
            transaction.rollback();
        }
        return invoices;
    }
}
