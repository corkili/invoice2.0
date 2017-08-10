package org.hld.invoice.test;

import junit.framework.TestCase;
import lombok.extern.log4j.Log4j;
import org.hld.invoice.dao.InvoiceDao;
import org.hld.invoice.entity.Invoice;
import org.hld.invoice.entity.InvoiceDetail;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.sql.Date;

/**
 * Created by 李浩然 On 2017/8/10.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:/META-INF/applicationContext.xml", "classpath:/META-INF/spring-mvc.xml"})
@Log4j
public class InvoiceDaoTest extends TestCase {
    @Autowired
    private InvoiceDao invoiceDao;

    @Test
    public void testAll() {
        Invoice invoice = new Invoice();
        invoice.setInvoiceId("25733284");
        invoice.setInvoiceCode("8724257851");
        invoice.setInvoiceDate(new Date(System.currentTimeMillis()));
        invoice.setBuyerName("四川大学软件学院");
        invoice.setBuyerId("");
        invoice.setSellerName("保定市品诺商贸有限公司");
        invoice.setSellerId("");
        invoice.setTotalAmount(72d);
        invoice.setTotalTax(7.92);
        invoice.setTotal(79.92);
        invoice.setRemark("");

        InvoiceDetail detail = new InvoiceDetail();
        detail.setDetailName("水壶");
        detail.setSpecification("");
        detail.setUnitName("个");
        detail.setQuantity(1);
        detail.setUnitPrice(12.5);
        detail.setAmount(12.5);
        detail.setTaxRate(0.11);
        detail.setTaxSum(1.375);
        invoice.getDetails().add(detail);

        detail = new InvoiceDetail();
        detail.setDetailName("水壶袋子");
        detail.setSpecification("");
        detail.setUnitName("个");
        detail.setQuantity(5);
        detail.setUnitPrice(11.9);
        detail.setAmount(59.5);
        detail.setTaxRate(0.11);
        detail.setTaxSum(6.545);
        invoice.getDetails().add(detail);

        log.info("init invoice: " + invoice);

        Long id = invoiceDao.save(invoice);
        log.info("saved invoice: " + invoice);

        invoice = invoiceDao.get(id);
        log.info("get by id: " + invoice);

        invoice = invoiceDao.findInvoices(false, null, null,
                "invoiceId", "25733284", "invoiceCode", "8724257851").get(0);
        log.info("get by unique: " + invoice);

        invoice.setRemark("Test update");
        log.info("modify invoice: " + invoice);
        invoiceDao.saveOrUpdate(invoice);
        invoice = invoiceDao.get(id);
        log.info("modify invoice: " + invoice);

        invoice.getDetails().get(0).setSpecification("这是一个水袋");
        log.info("modify invoiceDetail: " + invoice);
        invoiceDao.saveOrUpdate(invoice);
        invoice = invoiceDao.get(id);
        log.info("modify invoiceDetail: " + invoice);

        detail = invoice.getDetails().remove(invoice.getDetails().size() - 1);
        log.info("remove invoiceDetail: " + invoice);
        invoiceDao.saveOrUpdate(invoice);
        invoice = invoiceDao.get(id);
        log.info("remove invoiceDetail: " + invoice);

        detail.setDetailId(null);
        detail.setDetailName("新袋子");
        invoice.getDetails().add(detail);
        log.info("add invoiceDetail: " + invoice);
        invoiceDao.saveOrUpdate(invoice);
        invoice = invoiceDao.get(id);
        log.info("add invoiceDetail: " + invoice);
    }

    @Test
    public void testDelete() {
        invoiceDao.delete(1L);
        Invoice invoice = invoiceDao.get(1L);
        log.info("invoice: " + invoice);
        invoiceDao.delete(invoice);
        log.info("deleted invoice: " + invoice);
        log.info(invoiceDao.get(1L) == null);
        log.info(invoiceDao.findInvoices(false, null, null,
                "invoiceId", "25733284", "invoiceCode", "8724257851").size());
    }

    @Test
    public void testNull() {
        invoiceDao.save(null);
        invoiceDao.get(null);
        invoiceDao.saveOrUpdate(null);
        invoiceDao.update(null);
        invoiceDao.merge(null);
        invoiceDao.delete((Invoice)null);
        invoiceDao.delete((Long)null);
    }
}
