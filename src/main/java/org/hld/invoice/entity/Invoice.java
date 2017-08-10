package org.hld.invoice.entity;

import lombok.Data;

import javax.persistence.*;
import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by 李浩然 On 2017/8/8.
 */
@Data
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

    @OneToMany(fetch = FetchType.EAGER, targetEntity = InvoiceDetail.class, cascade = CascadeType.ALL)
    @JoinColumns(value = {@JoinColumn(name = "invoice_unique_id", referencedColumnName = "id")})
    @OrderBy(value = "detail_id desc")
    private List<InvoiceDetail> details;

    public Invoice() {
        details = new ArrayList<InvoiceDetail>();
    }

}
