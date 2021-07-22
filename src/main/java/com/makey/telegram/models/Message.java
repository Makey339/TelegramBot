package com.makey.telegram.models;


import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "Messages")
@Getter
@Setter
public class Message {

    @Id
    @Column(name ="msg_id")
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    @Column(name = "msg_text")
    private String text;

    @Column(name = "msg_date")
    private Date date;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @Column(name = "chat_id")
    private Long chat_id;
}
