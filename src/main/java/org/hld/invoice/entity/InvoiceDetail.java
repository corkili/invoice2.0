package org.hld.invoice.entity;

import javax.persistence.*;

/**
 * Created by 李浩然 On 2017/8/8.
 */
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

    @Version
    private int version;

    public Long getDetailId() {
        return detailId;
    }

    public void setDetailId(Long detailId) {
        this.detailId = detailId;
    }

    public String getDetailName() {
        return detailName;
    }

    public void setDetailName(String detailName) {
        this.detailName = detailName;
    }

    public String getSpecification() {
        return specification;
    }

    public void setSpecification(String specification) {
        this.specification = specification;
    }

    public String getUnitName() {
        return unitName;
    }

    public void setUnitName(String unitName) {
        this.unitName = unitName;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public Double getUnitPrice() {
        return unitPrice;
    }

    public void setUnitPrice(Double unitPrice) {
        this.unitPrice = unitPrice;
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public Double getTaxRate() {
        return taxRate;
    }

    public void setTaxRate(Double taxRate) {
        this.taxRate = taxRate;
    }

    public Double getTaxSum() {
        return taxSum;
    }

    public void setTaxSum(Double taxSum) {
        this.taxSum = taxSum;
    }

    public int getVersion() {
        return version;
    }

    public void setVersion(int version) {
        this.version = version;
    }
}
