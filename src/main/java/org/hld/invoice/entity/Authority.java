package org.hld.invoice.entity;

import javax.persistence.*;

/**
 * Created by 李浩然 On 2017/8/8.
 */
@Entity
@Table(name = "authority")
public class Authority {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    private Integer id;

    @Column(name = "query_invoice", nullable = false)
    private Boolean queryInvoice;

    @Column(name = "modify_invoice", nullable = false)
    private Boolean modifyInvoice;

    @Column(name = "add_invoice", nullable = false)
    private Boolean addInvoice;

    @Column(name = "remove_invoice", nullable = false)
    private Boolean removeInvoice;

    @Column(name = "query_report", nullable = false)
    private Boolean queryReport;

    @Column(name = "manager_user", nullable = false)
    private Boolean manageUser;

    @Version
    private int version;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Boolean getQueryInvoice() {
        return queryInvoice;
    }

    public void setQueryInvoice(Boolean queryInvoice) {
        this.queryInvoice = queryInvoice;
    }

    public Boolean getModifyInvoice() {
        return modifyInvoice;
    }

    public void setModifyInvoice(Boolean modifyInvoice) {
        this.modifyInvoice = modifyInvoice;
    }

    public Boolean getAddInvoice() {
        return addInvoice;
    }

    public void setAddInvoice(Boolean addInvoice) {
        this.addInvoice = addInvoice;
    }

    public Boolean getRemoveInvoice() {
        return removeInvoice;
    }

    public void setRemoveInvoice(Boolean removeInvoice) {
        this.removeInvoice = removeInvoice;
    }

    public Boolean getQueryReport() {
        return queryReport;
    }

    public void setQueryReport(Boolean queryReport) {
        this.queryReport = queryReport;
    }

    public Boolean getManageUser() {
        return manageUser;
    }

    public void setManageUser(Boolean manageUser) {
        this.manageUser = manageUser;
    }

    public int getVersion() {
        return version;
    }

    public void setVersion(int version) {
        this.version = version;
    }

}
