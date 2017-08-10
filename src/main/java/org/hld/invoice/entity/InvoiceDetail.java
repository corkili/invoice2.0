package org.hld.invoice.entity;

import lombok.Data;

import javax.persistence.*;

/**
 * Created by 李浩然 On 2017/8/8.
 */
@Data
@Entity(name = "InvoiceDetail")
@Table(name = "detail")
public class InvoiceDetail {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "detail_id")
    private Long detailId;                  // 明细编号

    @Column(name = "detail_name")
    private String detailName;              // 明细名称

    @Column(name = "specification")
    private String specification;           // 规格型号

    @Column(name = "unit_name")
    private String unitName;                // 单位

    @Column(name = "quantity")
    private Integer quantity;               // 数量

    @Column(name = "unit_price")
    private Double unitPrice;               // 单价

    @Column(name = "amount")
    private Double amount;                  // 金额

    @Column(name = "tax_rate")
    private Double taxRate;                 // 税率

    @Column(name = "tax_sum")
    private Double taxSum;                  // 税额

}
