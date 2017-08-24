package org.hld.invoice.dao;

import org.hld.invoice.entity.Invoice;

import java.sql.Date;
import java.util.List;
import java.util.Map;

/**
 * Created by 李浩然 On 2017/8/9.
 */
public interface InvoiceDao  extends BaseDao<Invoice, Long> {
    List<Invoice> findInvoices(boolean fuzzy, Date start, Date end, String... params) throws IllegalArgumentException;

    Map<String, String> findNames();
}
