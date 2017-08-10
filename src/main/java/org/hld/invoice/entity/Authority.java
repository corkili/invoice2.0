package org.hld.invoice.entity;

import lombok.Data;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.*;

/**
 * Created by 李浩然 On 2017/8/8.
 */
@Data
@Entity(name = "Authority")
@Table(name = "authority")
@Transactional(rollbackFor = Exception.class)
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

}
