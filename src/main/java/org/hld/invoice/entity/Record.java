package org.hld.invoice.entity;

import lombok.Data;

import javax.persistence.*;
import java.util.Date;

/**
 * Created by 李浩然 On 2017/8/10.
 */
@Data
@Entity
@Table(name = "records")
public class Record {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "ip", length = 50, nullable = false)
    private String ip;

    @Column(name = "address", nullable = false)
    private String address;

    @Temporal(value = TemporalType.TIMESTAMP)
    @Column(name = "time", nullable = false)
    private Date time;

    @Column(name = "user_id", nullable = false)
    private Integer userId;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "email", nullable = false)
    private String email;

    @Column(name = "content", columnDefinition = "text", nullable = false)
    private String content;
}
