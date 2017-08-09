package org.hld.invoice.entity;

import javax.persistence.*;
import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by 李浩然 On 2017/8/8.
 */
@Entity(name = "Invoice")
@Table(name = "invoice", uniqueConstraints = {@UniqueConstraint(columnNames = {"invoice_code","invoice_id"})})
public class Invoice {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    private Long id;

    @Column(name = "invoice_id", length = 8, nullable = false)
    private String invoiceId;

    @Column(name = "invoice_code", length = 12, nullable = false)
    private String invoiceCode;

    @Column(name = "invoice_date")
    private Date invoiceDate;

    @Column(name = "buyer_name")
    private String buyerName;

    @Column(name = "buyer_id")
    private String buyerId;

    @Column(name = "seller_name")
    private String sellerName;

    @Column(name = "seller_id")
    private String sellerId;

    @Column(name = "total_amount")
    private Double totalAmount;

    @Column(name = "total_tax")
    private Double totalTax;

    @Column(name = "total")
    private Double total;

    @Column(name = "remark", columnDefinition = "text")
    private String remark;

    @Version
    private int version;

    @OneToMany(fetch = FetchType.EAGER, targetEntity = InvoiceDetail.class, cascade = CascadeType.ALL)
    @JoinColumns(value = {@JoinColumn(name = "invoice_id", referencedColumnName = "id")})
    @OrderBy(value = "detail_id desc")
    private List<InvoiceDetail> details;

    public Invoice() {
        details = new ArrayList<InvoiceDetail>();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getInvoiceId() {
        return invoiceId;
    }

    public void setInvoiceId(String invoiceId) {
        this.invoiceId = invoiceId;
    }

    public String getInvoiceCode() {
        return invoiceCode;
    }

    public void setInvoiceCode(String invoiceCode) {
        this.invoiceCode = invoiceCode;
    }

    public Date getInvoiceDate() {
        return invoiceDate;
    }

    public void setInvoiceDate(Date invoiceDate) {
        this.invoiceDate = invoiceDate;
    }

    public String getBuyerName() {
        return buyerName;
    }

    public void setBuyerName(String buyerName) {
        this.buyerName = buyerName;
    }

    public String getBuyerId() {
        return buyerId;
    }

    public void setBuyerId(String buyerId) {
        this.buyerId = buyerId;
    }

    public String getSellerName() {
        return sellerName;
    }

    public void setSellerName(String sellerName) {
        this.sellerName = sellerName;
    }

    public String getSellerId() {
        return sellerId;
    }

    public void setSellerId(String sellerId) {
        this.sellerId = sellerId;
    }

    public Double getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(Double totalAmount) {
        this.totalAmount = totalAmount;
    }

    public Double getTotalTax() {
        return totalTax;
    }

    public void setTotalTax(Double totalTax) {
        this.totalTax = totalTax;
    }

    public Double getTotal() {
        return total;
    }

    public void setTotal(Double total) {
        this.total = total;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public int getVersion() {
        return version;
    }

    public void setVersion(int version) {
        this.version = version;
    }

    public List<InvoiceDetail> getDetails() {
        return details;
    }

    public void setDetails(List<InvoiceDetail> details) {
        this.details = details;
    }

}
