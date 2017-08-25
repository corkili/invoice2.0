package org.hld.invoice.entity;

import lombok.Data;

import javax.persistence.*;
import java.sql.Blob;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by 李浩然 On 2017/8/8.
 */
@Data
@Entity(name = "User")
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    private Integer id;

    @Column(name = "email", unique = true, nullable = false)
    private String email;

    @Column(name = "password", nullable = false)
    private String password;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "job_id", nullable = false)
    private String jobId;

    @Column(name = "phone", nullable = false)
    private String phone;

    @Column(name = "isSuperManager", nullable = false)
    private Boolean isSuperManager;

    @Column(name = "isManager", nullable = false)
    private Boolean isManager;

    @Column(name = "enabled", nullable = false)
    private Boolean enabled;

    @Column(name = "verification_code")
    private String verificationCode;

    @Temporal(value = TemporalType.TIMESTAMP)
    @Column(name = "create_time")
    private Date createTime;

    @Temporal(value = TemporalType.TIMESTAMP)
    @Column(name = "verify_time")
    private Date verifyTime;

    @Lob
    @Basic(fetch = FetchType.LAZY)
    @Column(name = "image")
    private Blob image;

    @OneToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinColumn(name = "authority_id", unique = true)
    private Authority authority;

}

