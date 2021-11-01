package com.openpayd.exchange.entity;

import lombok.Data;

import javax.persistence.*;
import java.util.Date;

import static javax.persistence.GenerationType.IDENTITY;

@Entity
@Table(name = "conversion")
@Data
public class Conversion {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Long id;

    private double amount;

    private String source;

    private String target;

    @Temporal(TemporalType.DATE)
    @Column(name = "created_date")
    private Date createdDate;

}
