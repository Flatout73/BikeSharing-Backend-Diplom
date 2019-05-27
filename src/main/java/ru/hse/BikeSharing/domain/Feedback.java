package ru.hse.BikeSharing.domain;


import lombok.Data;

import javax.persistence.*;

@Entity
@Table
@Data
public class Feedback extends AuditModel {

    @Id
    @GeneratedValue
    Long id;

    String address;
    String text;

    @OneToOne(fetch = FetchType.EAGER)
    private Bike bike;

    @OneToOne(fetch = FetchType.EAGER)
    private User user;
}
