package org.hld.invoice.entity;

import lombok.Data;

import javax.persistence.*;
import java.util.Date;

/**
 * Created by 李浩然 On 2017/8/10.
 */
@Data
@Entity
@Table(name = "record")
public class Record {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "ip", length = 50)
    private String ip;

    @Temporal(value = TemporalType.TIMESTAMP)
    @Column(name = "time")
    private Date time;

    @Column(name = "email")
    private String email;

    @Column(name = "content", columnDefinition = "text")
    private String content;
}
