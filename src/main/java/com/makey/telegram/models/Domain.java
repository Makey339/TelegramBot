package com.makey.telegram.models;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "domain")
@Getter
@Setter
public class Domain {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name ="domainname")
    private String domainname;

    @Column(name = "hotness")
    private int hotness;

    @Column(name = "price")
    private int price;

    @Column(name = "x_value")
    private int x_value;

    @Column(name = "yandex_tic")
    private int yandex_tic;

    @Column(name = "links")
    private int links;

    @Column(name = "visitors")
    private int visiotrs;

    @Column(name = "registrar")
    private String registrar;

    @Column(name = "yearsold")
    private int old;

    @Column(name = "delete_date")
    private Date delete_date;

    @Column(name = "rkn")
    private boolean rkn;

    @Column(name = "judicial")
    private boolean judicial;

    @Column(name = "block")
    private boolean block;

}
