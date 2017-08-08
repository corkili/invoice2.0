package org.hld.invoice.entity;

import javax.persistence.*;
import java.util.Date;

/**
 * Created by 李浩然 On 2017/8/8.
 */
/*
@NamedQueries(value = {
        @NamedQuery(name = "all user", query = " select u from User u ")
})*/
@Entity
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

    @Version
    private int version;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "authority_id", unique = true)
    private Authority authority;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getJobId() {
        return jobId;
    }

    public void setJobId(String jobId) {
        this.jobId = jobId;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public Boolean getManager() {
        return isManager;
    }

    public void setManager(Boolean manager) {
        isManager = manager;
    }

    public Boolean getEnabled() {
        return enabled;
    }

    public void setEnabled(Boolean enabled) {
        this.enabled = enabled;
    }

    public String getVerificationCode() {
        return verificationCode;
    }

    public void setVerificationCode(String verificationCode) {
        this.verificationCode = verificationCode;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getVerifyTime() {
        return verifyTime;
    }

    public void setVerifyTime(Date verifyTime) {
        this.verifyTime = verifyTime;
    }

    public int getVersion() {
        return version;
    }

    public void setVersion(int version) {
        this.version = version;
    }

    public Authority getAuthority() {
        return authority;
    }

    public void setAuthority(Authority authority) {
        this.authority = authority;
    }
}

